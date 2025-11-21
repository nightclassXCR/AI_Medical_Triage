package com.dd.ai_medical_triage.config;

import com.dd.ai_medical_triage.tool.MedicalTools;
import com.dd.ai_medical_triage.tool.PatientTools;
import com.dd.ai_medical_triage.tool.RegisterTools;
import com.dd.ai_medical_triage.tool.SymptomExtractTool;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.advisor.MessageChatMemoryAdvisor;
import org.springframework.ai.chat.memory.ChatMemory;
import org.springframework.ai.openai.OpenAiChatModel;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import static com.dd.ai_medical_triage.utils.AIConstants.*;

/**
 * AI 客户端配置
 */
@Configuration
public class AIConfig {

    public final ChatMemory chatMemory;

    public AIConfig(ChatMemory chatMemory) {
        this.chatMemory = chatMemory;
    }

    @Bean
    public ChatClient chatClient(OpenAiChatModel model) {
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
                .defaultTools(new PatientTools(), new MedicalTools(), new SymptomExtractTool(),new RegisterTools())
                .build();

    }
}
