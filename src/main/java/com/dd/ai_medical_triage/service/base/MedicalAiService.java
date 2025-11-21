package com.dd.ai_medical_triage.service.base;

import java.util.List;
import java.util.Map;

/**
 * AI导诊服务接口
 * 封装与AI交互的核心业务能力，覆盖问诊、科室匹配、医生推荐、挂号全流程
 */
public interface MedicalAiService {

    /**
     * 1. 多轮问诊交互（核心方法）
     * 功能：与患者多轮对话，采集关键病情信息，并自动推进流程（科室匹配→医生推荐→挂号）
     * @param userId 用户唯一标识（用于维护对话上下文）
     * @param userInput 患者当前输入（病情描述/操作指令）
     * @return AI的响应内容（含引导/结果）
     */
    String multiRoundConsult(Long userId, String userInput);

    /**
     * 2. 采集患者关键病情信息（单独方法，可选）
     * 功能：主动引导患者补充病情细节（如症状持续时间、伴随症状等）
     * @param userId 用户唯一标识
     * @param initialSymptom 患者初始症状
     * @return 引导患者补充信息的AI话术
     */
    String collectPatientInfo(Long userId, String initialSymptom);

    /**
     * 3. 根据病情匹配科室
     * 功能：结合患者病情，调用工具匹配对应的就诊科室
     * @param userId 用户唯一标识
     * @param symptom 患者症状描述
     * @return 匹配结果（含推荐科室）
     */
    String matchDepartment(Long userId, String symptom);

    /**
     * 4. 根据科室和病情推荐坐诊医生
     * 功能：结合科室、病情，筛选符合条件的坐诊医生列表
     * @param userId 用户唯一标识
     * @param department 目标科室
     * @param symptom 患者症状（用于匹配擅长该病症的医生）
     * @return 医生列表（含姓名、坐诊时间、擅长领域）
     */
    List<Map<String, String>> recommendDoctors(Long userId, String department, String symptom);

    /**
     * 5. 调用工具完成挂号
     * 功能：根据患者选择的医生，触发ToolCalling完成挂号操作
     * @param userId 用户唯一标识
     * @param doctorId 医生ID
     * @return 挂号结果（成功/失败信息）
     */
    String registerDoctor(Long userId, Long doctorId);

    /**
     * 6. 清空用户对话上下文
     * 功能：重置指定用户的对话历史（如用户结束问诊后）
     * @param userId 用户唯一标识
     */
    void clearChatContext(Long userId);
}
