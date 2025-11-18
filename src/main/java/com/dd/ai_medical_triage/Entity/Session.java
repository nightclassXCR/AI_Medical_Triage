package com.dd.ai_medical_triage.Entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Session {
    private int sessionId;
    private int patientId;
    private LocalDateTime createdTime;
    private String status;
}
