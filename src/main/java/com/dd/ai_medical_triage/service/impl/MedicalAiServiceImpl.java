package com.dd.ai_medical_triage.service.impl;

import com.dd.ai_medical_triage.entity.Appointment;
import com.dd.ai_medical_triage.enums.ErrorCode.ErrorCode;
import com.dd.ai_medical_triage.exception.BusinessException;
import com.dd.ai_medical_triage.service.base.MedicalAiService;
import com.dd.ai_medical_triage.tool.DateTimeTools;
import com.dd.ai_medical_triage.vo.ResultVO;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.messages.AssistantMessage;
import org.springframework.ai.chat.messages.Message;
import org.springframework.ai.chat.messages.UserMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * AI 导诊服务实现类（核心业务逻辑）
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class MedicalAiServiceImpl implements MedicalAiService {

    // 注入 AI 客户端（已注册所有工具）
    @Autowired
    private final ChatClient medicalChatClient;
    // 注入 Redis 模板（存储对话上下文）
    private final StringRedisTemplate stringRedisTemplate;
    // 注入时间工具类（获取当前时间）
    private final DateTimeTools dateTimeTools;
    // Jackson 工具（序列化/反序列化）
    private final ObjectMapper objectMapper = new ObjectMapper();

    // ------------------------------ 常量定义 ------------------------------
    // 系统提示词（引导 AI 按流程调用工具）
    private static final String SYSTEM_PROMPT = """
            你是专业的医疗AI分诊助手，需严格按以下流程为患者服务：
            1. 若患者未提供个人信息，先调用 PatientTools 记录姓名、年龄、性别等基本信息（自动生成病历号）；
            2. 调用 StartSessionTool 启动新的问诊会话；
            3. 引导患者描述症状，调用 SymptomExtractTool 记录症状、严重程度及持续时间；
            4. 根据症状，调用 MedicalTools 查询匹配的科室和医生；
            5. 确认患者就诊意向后，调用 RegisterTools 完成挂号；
            6. 所有工具调用结果需以自然语言反馈给患者，无需暴露技术细节；
            7. 可调用 DateTimeTools 获取当前时间，用于挂号时间确认。
            """;
    // Redis 键前缀（区分不同用户的对话）
    private static final String REDIS_KEY_PREFIX = "medical:chat:context:";
    // 对话过期时间（24小时）
    private static final Duration CONTEXT_EXPIRE = Duration.ofHours(24);
    // 本地缓存（减少 Redis 访问次数）
    private final Map<Long, List<Message>> localChatContext = new ConcurrentHashMap<>();

    // ------------------------------ 1. 多轮问诊交互（核心方法） ------------------------------
    @Override
    public String multiRoundConsult(Long userId, String userInput) {
        try {
            // 1. 参数校验
            if (userId == null || !StringUtils.hasText(userInput)) {
                log.warn("用户ID或输入为空：userId={}, userInput={}", userId, userInput);
                throw new BusinessException(ErrorCode.PARAM_NULL);
            }

            // 2. 获取/初始化对话上下文
            List<Message> chatContext = getChatContext(userId);

            // 3. 构建提示（包含系统指令+历史对话+当前输入）
            String prompt = String.format("""
                    系统指令：%s
                    对话历史：%s
                    患者当前输入：%s
                    """, SYSTEM_PROMPT, formatContextToText(chatContext), userInput);

            // 4. 调用 AI 并获取响应（自动触发工具调用）
            String aiResponse = medicalChatClient.prompt()
                    .user(prompt)
                    .call()
                    .content();

            // 5. 更新对话上下文（添加当前输入和AI响应）
            chatContext.add(new UserMessage(userInput));
            chatContext.add(new AssistantMessage(aiResponse));
            // 限制上下文长度（避免 Token 超限）
            if (chatContext.size() > 20) {
                chatContext.subList(0, chatContext.size() - 20).clear();
            }

            // 6. 保存上下文到本地缓存和 Redis
            saveChatContext(userId, chatContext);

            log.info("多轮问诊完成：userId={}, userInput={}, aiResponse={}", userId, userInput, aiResponse);
            return aiResponse;

        } catch (BusinessException e) {
            throw e;
        } catch (Exception e) {
            log.error("多轮问诊失败：userId={}, userInput={}", userId, userInput, e);
            throw new BusinessException(ErrorCode.AI_SERVICE_FAILS);
        }
    }

    // ------------------------------ 2. 采集患者病情信息 ------------------------------
    @Override
    public String collectPatientInfo(Long userId, String initialSymptom) {
        try {
            // 1. 参数校验
            if (userId == null || !StringUtils.hasText(initialSymptom)) {
                throw new BusinessException(ErrorCode.PARAM_NULL);
            }

            // 2. 构建提示（引导 AI 收集症状细节）
            String prompt = String.format("""
                    患者初始症状：%s，请引导患者补充以下信息：
                    1. 症状具体表现（如咳嗽是否有痰、疼痛位置等）；
                    2. 症状严重程度（轻微/中等/严重）；
                    3. 症状持续时间（如：2天/1周）；
                    收集完成后调用 SymptomExtractTool 保存记录，并告知患者。
                    """, initialSymptom);

            // 3. 调用 AI 获取引导话术
            String aiResponse = medicalChatClient.prompt()
                    .system(SYSTEM_PROMPT)
                    .user(prompt)
                    .call()
                    .content();

            // 4. 更新对话上下文
            List<Message> chatContext = getChatContext(userId);
            chatContext.add(new UserMessage("初始症状：" + initialSymptom));
            chatContext.add(new AssistantMessage(aiResponse));
            saveChatContext(userId, chatContext);

            return aiResponse;

        } catch (BusinessException e) {
            throw e;
        } catch (Exception e) {
            log.error("采集病情信息失败：userId={}, initialSymptom={}", userId, initialSymptom, e);
            throw new BusinessException(ErrorCode.AI_SERVICE_FAILS);
        }
    }

    // ------------------------------ 3. 根据病情匹配科室 ------------------------------
    @Override
    public String matchDepartment(Long userId, String symptom) {
        try {
            // 1. 参数校验
            if (userId == null || !StringUtils.hasText(symptom)) {
                throw new BusinessException(ErrorCode.PARAM_NULL);
            }

            // 2. 构建提示（调用 MedicalTools 匹配科室）
            String prompt = String.format("""
                    患者症状：%s，请按以下步骤处理：
                    1. 调用 MedicalTools 的 queryDepartment 方法获取所有科室；
                    2. 根据症状匹配最适合的科室（如感冒咳嗽匹配呼吸内科）；
                    3. 返回科室名称及推荐理由（用自然语言描述，无需技术细节）。
                    """, symptom);

            // 3. 调用 AI 获取匹配结果
            String aiResponse = medicalChatClient.prompt()
                    .system(SYSTEM_PROMPT)
                    .user(prompt)
                    .call()
                    .content();

            // 4. 更新对话上下文
            List<Message> chatContext = getChatContext(userId);
            chatContext.add(new UserMessage("症状：" + symptom + "，匹配科室"));
            chatContext.add(new AssistantMessage(aiResponse));
            saveChatContext(userId, chatContext);

            return aiResponse;

        } catch (BusinessException e) {
            throw e;
        } catch (Exception e) {
            log.error("匹配科室失败：userId={}, symptom={}", userId, symptom, e);
            throw new BusinessException(ErrorCode.AI_SERVICE_FAILS);
        }
    }

    // ------------------------------ 4. 根据科室和病情推荐医生 ------------------------------
    @Override
    public List<Map<String, String>> recommendDoctors(Long userId, String department, String symptom) {
        try {
            // 1. 参数校验
            if (userId == null || !StringUtils.hasText(department) || !StringUtils.hasText(symptom)) {
                throw new BusinessException(ErrorCode.PARAM_NULL);
            }

            // 2. 构建提示（调用 MedicalTools 查询医生+推荐）
            String prompt = String.format("""
                    科室：%s，患者症状：%s，请按以下步骤处理：
                    1. 调用 MedicalTools 的 queryDoctor 方法获取该科室所有医生；
                    2. 调用 queryDoctorSchedule 方法获取医生出诊时间；
                    3. 根据症状推荐擅长该病症的医生（如咳嗽推荐擅长呼吸道疾病的医生）；
                    4. 结果以 JSON 格式返回，JSON 结构为 List<Map>，Map 包含 key：
                       - doctorName（医生姓名）
                       - schedule（出诊时间，如：周一至周五 8:00-12:00）
                       - specialty（擅长领域，如：呼吸道感染、肺炎）
                    5. 仅返回 JSON，不要添加其他自然语言。
                    """, department, symptom);

            // 3. 调用 AI 并解析结果
            String aiResponse = medicalChatClient.prompt()
                    .system(SYSTEM_PROMPT)
                    .user(prompt)
                    .call()
                    .content();
            // 反序列化 JSON 为 List<Map>
            List<Map<String, String>> doctorList = objectMapper.readValue(
                    aiResponse,
                    new TypeReference<List<Map<String, String>>>() {}
            );

            // 4. 更新对话上下文
            List<Message> chatContext = getChatContext(userId);
            chatContext.add(new UserMessage("科室：" + department + "，症状：" + symptom + "，推荐医生"));
            chatContext.add(new AssistantMessage("已为您推荐该科室医生，详情如下：" + aiResponse));
            saveChatContext(userId, chatContext);

            return doctorList;

        } catch (BusinessException e) {
            throw e;
        } catch (Exception e) {
            log.error("推荐医生失败：userId={}, department={}, symptom={}", userId, department, symptom, e);
            throw new BusinessException(ErrorCode.AI_SERVICE_FAILS);
        }
    }

    // ------------------------------ 5. 调用工具完成挂号 ------------------------------
    @Override
    public String registerDoctor(Long userId, Long doctorId) {
        try {
            // 1. 参数校验
            if (userId == null || doctorId == null) {
                throw new BusinessException(ErrorCode.PARAM_NULL);
            }

            // 2. 从上下文获取患者病历号（必须先记录患者信息）
            String caseNumber = getCaseNumberFromContext(userId);
            if (!StringUtils.hasText(caseNumber)) {
                throw new BusinessException(ErrorCode.AI_PARSE_FAILS, "未找到患者病历号");
            }

            // 3. 构建挂号请求（模拟患者信息，实际可从上下文提取更多字段）
            Appointment appointment = new Appointment();
//            appointment.setCaseNumber(caseNumber);
//            appointment.setDoctorId(doctorId);
//            appointment.setPatientName(getPatientNameFromContext(userId)); // 从上下文提取患者姓名
//            appointment.setPhoneNumber(getPatientPhoneFromContext(userId)); // 从上下文提取电话
//            appointment.setVisitTime(getNextVisitTime(doctorId)); // 模拟获取最近出诊时间

            // 4. 构建提示（调用 RegisterTools 完成挂号）
            String currentTime = dateTimeTools.getCurrentTime();
            String prompt = "";
//            String prompt = String.format("""
//                    挂号请求信息：
//                    - 病历号：%s
//                    - 医生ID：%s
//                    - 患者姓名：%s
//                    - 联系电话：%s
//                    - 就诊时间：%s
//                    - 当前时间：%s
//                    请调用 RegisterTools 的 appointmentFunction 方法完成挂号，
//                    返回挂号成功信息（含病历号、医生姓名、就诊时间），用自然语言描述。
//                    """, caseNumber, doctorId, appointment.getPatientName(),
//                    appointment.getPhoneNumber(), appointment.getVisitTime(), currentTime);

            // 5. 调用 AI 完成挂号
            String aiResponse = medicalChatClient.prompt()
                    .system(SYSTEM_PROMPT)
                    .user(prompt)
                    .call()
                    .content();

            // 6. 更新对话上下文
            List<Message> chatContext = getChatContext(userId);
            chatContext.add(new UserMessage("挂号医生ID：" + doctorId));
            chatContext.add(new AssistantMessage(aiResponse));
            saveChatContext(userId, chatContext);

            return aiResponse;

        } catch (BusinessException e) {
            throw e;
        } catch (Exception e) {
            log.error("挂号失败：userId={}, doctorId={}", userId, doctorId, e);
            throw new BusinessException(ErrorCode.AI_SERVICE_FAILS);
        }
    }

    // ------------------------------ 6. 清空用户对话上下文 ------------------------------
    @Override
    public void clearChatContext(Long userId) {
        try {
            // 1. 参数校验
            if (userId == null) {
                throw new BusinessException(ErrorCode.PARAM_NULL);
            }

            // 2. 清除本地缓存和 Redis 缓存
            localChatContext.remove(userId);
            stringRedisTemplate.delete(REDIS_KEY_PREFIX + userId);

            log.info("对话上下文已清空：userId={}", userId);
        } catch (BusinessException e) {
            throw e;
        } catch (Exception e) {
            log.error("清空对话上下文失败：userId={}", userId, e);
            throw new BusinessException(ErrorCode.AI_CLEAR_CHAT_CONTENT_FAILS);
        }
    }

    // ------------------------------ 私有辅助方法（无需复制，内部使用） ------------------------------
    /**
     * 从本地缓存/Redis 获取对话上下文
     */
    private List<Message> getChatContext(Long userId) {
        // 1. 先查本地缓存
        List<Message> context = localChatContext.get(userId);
        if (context != null && !context.isEmpty()) {
            return context;
        }

        // 2. 本地无则从 Redis 加载
        String redisKey = REDIS_KEY_PREFIX + userId;
        String contextJson = stringRedisTemplate.opsForValue().get(redisKey);
        if (StringUtils.hasText(contextJson)) {
            try {
                context = objectMapper.readValue(contextJson, new TypeReference<List<Message>>() {});
                localChatContext.put(userId, context); // 同步到本地缓存
                return context;
            } catch (Exception e) {
                log.error("反序列化对话上下文失败：userId={}", userId, e);
            }
        }

        // 3. 均无则初始化
        return new ArrayList<>();
    }

    /**
     * 保存对话上下文到本地缓存和 Redis
     */
    private void saveChatContext(Long userId, List<Message> context) {
        try {
            // 1. 保存到本地缓存
            localChatContext.put(userId, context);

            // 2. 序列化后保存到 Redis（设置过期时间）
            String redisKey = REDIS_KEY_PREFIX + userId;
            String contextJson = objectMapper.writeValueAsString(context);
            stringRedisTemplate.opsForValue().set(redisKey, contextJson, CONTEXT_EXPIRE);
        } catch (Exception e) {
            log.error("保存对话上下文失败：userId={}", userId, e);
        }
    }

    /**
     * 格式化上下文为文本（供 AI 识别）
     */
    private String formatContextToText(List<Message> context) {
        if (context.isEmpty()) {
            return "无历史对话";
        }
        StringBuilder sb = new StringBuilder();
        for (Message msg : context) {
            if (msg instanceof UserMessage) {
                sb.append("患者：").append(msg.getText()).append("\n");
            } else if (msg instanceof AssistantMessage) {
                sb.append("助手：").append(msg.getText()).append("\n");
            }
        }
        return sb.toString();
    }

    /**
     * 从上下文提取患者病历号（简化实现，实际可优化为正则匹配）
     */
    private String getCaseNumberFromContext(Long userId) {
        List<Message> context = getChatContext(userId);
        for (Message msg : context) {
            String content = msg.getText();
            if (content.contains("病历号：")) {
                // 提取“病历号：”后的内容（如：病历号：C123456 → 提取 C123456）
                return content.split("病历号：")[1].split("，|。")[0].trim();
            }
        }
        return null;
    }

    /**
     * 从上下文提取患者姓名（辅助方法）
     */
    private String getPatientNameFromContext(Long userId) {
        List<Message> context = getChatContext(userId);
        for (Message msg : context) {
            String content = msg.getText();
            if (content.contains("患者姓名：")) {
                return content.split("患者姓名：")[1].split("，|。")[0].trim();
            }
        }
        return "未知";
    }

    /**
     * 从上下文提取患者电话（辅助方法）
     */
    private String getPatientPhoneFromContext(Long userId) {
        List<Message> context = getChatContext(userId);
        for (Message msg : context) {
            String content = msg.getText();
            if (content.contains("电话号码：")) {
                return content.split("电话号码：")[1].split("，|。")[0].trim();
            }
        }
        return "未知";
    }

    /**
     * 模拟获取医生最近出诊时间（实际需调用 MedicalTools 查询）
     */
    private LocalDateTime getNextVisitTime(Long doctorId) {
        // 简化实现：返回当前时间+1天（实际需从医生出诊表中查询）
        return LocalDateTime.now().plusDays(1);
    }
}