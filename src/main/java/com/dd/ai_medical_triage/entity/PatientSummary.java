package com.dd.ai_medical_triage.entity;

import lombok.Data;

import java.time.LocalDateTime;
/**
 * 患者问诊总结实体类
 * 对应数据库表：patient_summary
 * 患者问诊总结实体类
 */
@Data
public class PatientSummary {
    private int summaryId;
    private int patientId;
    private int appointmentId;
    private String summaryText;    // AI 问诊总结
    private LocalDateTime createdTime;
}
