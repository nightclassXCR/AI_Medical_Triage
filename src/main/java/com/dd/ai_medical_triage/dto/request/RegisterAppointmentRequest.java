package com.dd.ai_medical_triage.dto.request;


import lombok.Data;

@Data
public class RegisterAppointmentRequest {
    private int patientId;
    private int doctorId;
    private String symptoms;
    private String priority;
}
