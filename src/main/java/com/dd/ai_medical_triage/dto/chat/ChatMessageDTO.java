package com.dd.ai_medical_triage.dto.chat;


import lombok.Data;

@Data
public class ChatMessageDTO {
    private String id;
    private String role;
    private String content;
    private Long timestamp;
}
