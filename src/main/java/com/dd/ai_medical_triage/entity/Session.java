package com.dd.ai_medical_triage.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
/**
 * 会话实体类
 * 对应数据库表：session
 */

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Session {
    private int sessionId;
    private int patientId;
    private LocalDateTime createdTime;
    private String status;
}
