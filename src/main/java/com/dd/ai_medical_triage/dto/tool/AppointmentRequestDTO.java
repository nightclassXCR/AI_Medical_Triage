package com.dd.ai_medical_triage.dto.tool;


import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.util.List;

@Data
@Schema(description = "预约信息请求数据模型")
public class AppointmentRequestDTO {
    @Schema(description = "患者ID")
    private Long patientId;
    @Schema(description = "医生ID")
    private Long doctorId;
    @Schema(description = "排班ID")
    private Long scheduleId;
    @Schema(description = "预约时间")
    private String appointmentTime;
}
