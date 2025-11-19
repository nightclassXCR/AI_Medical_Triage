package com.dd.ai_medical_triage.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
/**
 * 医生实体类
 * 对应数据库表：doctor
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Doctor {
    private Long userId;
    private int doctorId;
    private String name;
    private String department;
    private String title;          // 主任、主治等
    private String phone;

}
