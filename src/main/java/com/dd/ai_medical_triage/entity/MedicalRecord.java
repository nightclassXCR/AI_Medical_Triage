package com.dd.ai_medical_triage.entity;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
/**
 * 医疗记录实体类
 * 对应数据库表：medical_record
 *
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class MedicalRecord {
    private int recordId;
    private int appointmentId;
    private int patientId;
    private int doctorId;
    private String diagnosis;
    private String prescription;
    private String suggestions;
    private LocalDateTime createdTime;
}
