package com.dd.ai_medical_triage.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
/**
 * 症状记录实体类
 * 对应数据库表：symptom_record
 *
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class SymptomRecord {
    private int symptomId;
    private int patientId;
    private String symptomText;        // 症状名称或描述
    private String severity;           // 轻度/中度/重度
    private String duration;           // 持续时间描述
    private LocalDateTime extractedTime;

}
