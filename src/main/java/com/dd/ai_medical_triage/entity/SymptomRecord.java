package com.dd.ai_medical_triage.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import jakarta.validation.constraints.NotBlank;
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
@TableName("symptom_record")
public class SymptomRecord {
    /**
     * 症状ID
     * 对应文档4 symptom_record表：symptom_id（int AI PK）
     */
    @TableId(value = "symptom_id",type = IdType.AUTO)
    @NotBlank(message = "症状ID不能为空")
    private Long symptomId;

    /**
     * 患者ID
     * 对应文档4 symptom_record表：patient_id（int）
     */
    private Long patientId;
    private String symptomText;        // 症状名称或描述
    private String severity;           // 轻度/中度/重度
    private String duration;           // 持续时间描述
    private LocalDateTime extractedTime;

}
