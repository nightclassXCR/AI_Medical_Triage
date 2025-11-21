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

@Configuration
public class AIConfig {

    public final ChatMemory chatMemory;

    public AIConfig(ChatMemory chatMemory) {
        this.chatMemory = chatMemory;
    }

    @Bean
    public ChatClient chatClient(OpenAiChatModel model) {
        return ChatClient
                .builder(model)
                .defaultSystem(MADICAL_TRIAGE_SYSTEM)
                .defaultSystem(TOOL_CALLING)
                .defaultSystem(SAFETY_BOUNDARIES)
                .defaultSystem(INTERACTION_RULES)
                .defaultAdvisors(MessageChatMemoryAdvisor.builder(chatMemory).build())
                .defaultTools(new PatientTools(), new MedicalTools(), new SymptomExtractTool(),new RegisterTools())
                .build();

    }


}
