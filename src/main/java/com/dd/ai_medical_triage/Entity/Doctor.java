package com.dd.ai_medical_triage.Entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Doctor {
    private int doctorId;
    private String name;
    private String department;
    private String title;          // 主任、主治等
    private String phone;

}
