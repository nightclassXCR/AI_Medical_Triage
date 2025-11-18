package com.dd.ai_medical_triage.Entity;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class PatientSummary {
    private int summaryId;
    private int patientId;
    private int appointmentId;
    private String summaryText;    // AI 问诊总结
    private LocalDateTime createdTime;
}
