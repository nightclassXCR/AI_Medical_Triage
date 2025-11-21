package com.dd.ai_medical_triage.entity;

import com.baomidou.mybatisplus.annotation.*;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
/**
 * 预约信息实体类
 * 对应数据库表：appointment
 *
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@TableName("appointment")
public class Appointment {

    /*
    *预约表的唯一标识
    * 对应文档4 appointment表：appointment_id（bigint AI PK）
    */
    @TableId(value = "appointment_id",type = IdType.AUTO)
    private Long appointmentId;

    /*
    * 患者id
    * 对应文档4 appointment表：patient_id（bigint）
    * */
    @NotBlank(message = "患者ID不能为空")
    private Long patientId;

    /*
    * 医生ID
    * 对应文档4 appointment表：doctor_id（bigint）
    */
    @NotBlank(message = "医生ID不能为空")
    private Long doctorId;

    /*
    * 排班ID
    * 对应文档4 appointment表：schedule_id（bigint）
    */
    private Long scheduleId;

    /*
    * 预约时间
    * 对应文档4 appointment表：appointment_time（datetime）
    */
    @NotBlank(message = "预约时间不能为空")
    private LocalDateTime appointmentTime;
    /*
    * 预约状态
    * 对应文档4 appointment表：status（tinyint）
    */
    // 状态: 0-待支付, 1-成功, 2-取消
    private Integer status;


}
