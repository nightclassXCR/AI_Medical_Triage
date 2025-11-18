package com.dd.ai_medical_triage.Entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;


@Data
@AllArgsConstructor
@NoArgsConstructor
public class MessageQueueLog {
    private Long messageId;
    private String eventType;
    private String payload;
    private String status;
    private LocalDateTime createdTime;
}
