package com.dd.ai_medical_triage.entity;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
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
public class Appointment {
    private int appointmentId;
    private int patientId;
    private int doctorId;
    private int scheduleId;
    private LocalDateTime appointmentTime;
    // 状态: 0-待支付, 1-成功, 2-取消
    private Integer status;


}
