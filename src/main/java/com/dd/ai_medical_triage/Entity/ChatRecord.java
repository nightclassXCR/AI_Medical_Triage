package com.dd.ai_medical_triage.Entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ChatRecord {
    private int chatId;
    private int patientId;
    private LocalDateTime timestamp;
    private String question;
    private String aiReply;
}
