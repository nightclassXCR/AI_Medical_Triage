package com.dd.ai_medical_triage.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import jakarta.validation.constraints.NotBlank;
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
@TableName("message_queue_log")
public class MessageQueueLog {
    /**
     * 消息ID
     * 对应文档4 message_queue_log表：message_id（bigint）
     */
    @TableId(value = "message_id",type = IdType.AUTO)
    private Long messageId;
    /**
     * 事件类型
     * 对应文档4 message_queue_log表：event_type（varchar(50)）
     */
    @NotBlank(message = "事件类型不能为空")
    private String eventType;

    private String payload;
    private String status;
    private LocalDateTime createdTime;
}
