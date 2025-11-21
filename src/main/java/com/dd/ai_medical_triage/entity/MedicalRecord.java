package com.dd.ai_medical_triage.entity;


import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import jakarta.validation.constraints.NotBlank;
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
@TableName("medical_record")
public class MedicalRecord {
    /*
    *医疗记录实体类
    * 对应数据库表：medical_record
    *
    */

    @TableId(value = "record_id",type = IdType.AUTO)
    @NotBlank(message = "医疗记录ID不能为空")
    private Long recordId;

    @TableField("appointment_id")
    @NotBlank(message = "预约ID不能为空")
    private Long appointmentId;

    @TableField("patient_id")
    @NotBlank(message = "患者ID不能为空")
    private Long patientId;

    @TableField("doctor_id")
    @NotBlank(message = "医生ID不能为空")
    private Long doctorId;
    /*
    * 处方
    **/
    private String diagnosis;

    private String prescription;
    private String suggestions;
    private LocalDateTime createdTime;
}
