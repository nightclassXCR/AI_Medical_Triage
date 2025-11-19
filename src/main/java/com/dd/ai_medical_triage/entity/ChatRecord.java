package com.dd.ai_medical_triage.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
/**
 * 会话实体类
 * 对应数据库表：chat_record
 * 聊天记录
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ChatRecord {
    private int chatId;
    private int patientId;
    private LocalDateTime timestamp;
    private String question;
    private String aiReply;
}
