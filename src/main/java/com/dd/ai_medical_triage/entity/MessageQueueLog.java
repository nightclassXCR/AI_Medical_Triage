package com.dd.ai_medical_triage.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
/**
 * 消息队列日志实体类
 * 对应数据库表：message_queue_log
 * 记录消息队列中的事件和状态
 */

@Data
@AllArgsConstructor
@NoArgsConstructor
public class MessageQueueLog {
    private Long messageId;
    private String eventType;
    private String payload;
    private String status;
    private LocalDateTime createdTime;
}
