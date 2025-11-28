package com.dd.ai_medical_triage.config;

import com.dd.ai_medical_triage.dao.repository.DoubleLayerChatMemoryRepository;
import com.dd.ai_medical_triage.dao.repository.RedisChatMemoryRepository;
import com.dd.ai_medical_triage.dao.repository.RedisChatMemoryRepositoryDialect;
import com.dd.ai_medical_triage.tool.*;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.advisor.MessageChatMemoryAdvisor;
import org.springframework.ai.chat.memory.ChatMemory;
import org.springframework.ai.chat.memory.ChatMemoryRepository;
import org.springframework.ai.chat.memory.MessageWindowChatMemory;
import org.springframework.ai.openai.OpenAiChatModel;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

import static com.dd.ai_medical_triage.utils.constants.AIConstants.*;

/**
 * AI 客户端配置
 */
@Configuration
public class AIConfig {

    /**
     * 聊天记忆存储库（使用Redis）
     * @param dialect 使用的聊天存储库方言
     * @return 聊天存储库
     */
    @Bean("redisChatMemoryRepository")
    public ChatMemoryRepository redisChatMemoryRepository(RedisChatMemoryRepositoryDialect dialect) {
        return new RedisChatMemoryRepository(dialect);
    }

    /**
     * 聊天记忆存储库（使用MySQL+Redis双层架构）
     * @return 聊天存储库
     */
    @Bean("doubleLayerChatMemoryRepository")
    @Primary
    public ChatMemoryRepository doubleLayerChatMemoryRepository() {
        return new DoubleLayerChatMemoryRepository();
    }

    /**
     * 聊天内存
     * @param chatMemoryRepository 聊天内存存储方式
     * @return 聊天内存
     */
    @Bean
    public ChatMemory chatMemory(ChatMemoryRepository chatMemoryRepository) {
        return MessageWindowChatMemory.builder()
                .chatMemoryRepository(chatMemoryRepository)
                .maxMessages(20)
                .build();
    }

    /**
     * 聊天模型
     * @return 聊天模型
     */
    @Bean
    public ChatClient chatClient(OpenAiChatModel model, ChatMemory chatMemory,
                                 PatientTools patientTools,
                                 DateTimeTools dateTimeTools,
                                 MedicalTools medicalTools,
                                 SymptomExtractTool symptomExtractTool,
                                 RegisterTools registerTools // 你的挂号工具
                                  ) {


        return ChatClient
                // 注入底层 Model
                .builder(model)
                // 默认系统提示词
                .defaultSystem(MADICAL_TRIAGE_SYSTEM)
                .defaultSystem(TOOL_CALLING)
                .defaultSystem(SAFETY_BOUNDARIES)
                .defaultSystem(INTERACTION_RULES)
                // 默认顾问
                .defaultAdvisors(MessageChatMemoryAdvisor.builder(chatMemory).build())
                // 默认注册工具
                .defaultTools(dateTimeTools,patientTools,medicalTools, symptomExtractTool,registerTools)
                .build();

    }
}
