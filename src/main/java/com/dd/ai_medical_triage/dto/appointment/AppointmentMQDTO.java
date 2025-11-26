package com.dd.ai_medical_triage.dto.appointment;


import lombok.Data;

@Data
public class AppointmentMQDTO {
    private String msgId;        // 用于 Redis 幂等
    private Long appointmentId;  // 挂号表主键
    private Long patientId;
    private Long doctorId;
    private String symptoms;
    private String priority;
}
