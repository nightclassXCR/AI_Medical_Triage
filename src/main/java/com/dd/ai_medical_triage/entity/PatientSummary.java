package com.dd.ai_medical_triage.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

import java.time.LocalDateTime;
/**
 * 患者问诊总结实体类
 * 对应数据库表：patient_summary
 * 患者问诊总结实体类
 */
@Data
@TableName("patient_summary")
public class PatientSummary {

    /**
     * 患者问诊总结ID
     * 对应文档4 patient_summary表：summary_id（bigint AI PK）
     */
    @TableId(value = "summary_id",type = IdType.AUTO)
    @NotBlank(message = "患者问诊总结ID不能为空")
    private Long summaryId;

    /**
     * 患者ID
     * 对应文档4 patient_summary表：patient_id（bigint）
     */
    @NotBlank(message = "患者ID不能为空")
    private Long patientId;

    /**
     * 预约ID
     * 对应文档4 patient_summary表：appointment_id（bigint）
     */
    @NotBlank(message = "预约ID不能为空")
    private Long appointmentId;
    /**
     * 患者问诊总结
     * 对应文档4 patient_summary表：summary_text（text）
     */
    private String summaryText;

        // AI 问诊总结
    private LocalDateTime createdTime;
}
