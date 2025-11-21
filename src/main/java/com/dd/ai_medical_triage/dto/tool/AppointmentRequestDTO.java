package com.dd.ai_medical_triage.dto.tool;


import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@Schema(description = "预约信息请求数据模型")
public class AppointmentRequestDTO {
    private Long patientId;
    private Long doctorId;
    private Long scheduleId;
    private String appointmentTime;
}
