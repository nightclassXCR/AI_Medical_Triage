package com.dd.ai_medical_triage.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.dd.ai_medical_triage.enums.SimpleEnum.ChatMemoryTypeEnum;
import lombok.Builder;
import lombok.Data;
import org.springframework.ai.chat.messages.MessageType;

import java.time.LocalDateTime;

/**
 * AI会话内存模块实体类
 */
@Data
@Builder
@TableName("chat_memory")
public class ChatMemory {

    /** 主键ID（自增） */
    @TableId(type = IdType.AUTO)  // 主键策略：自增
    private Long chatMemoryId;

    /** 会话ID */
    private String sessionId;

    /** 消息角色 */
    private ChatMemoryTypeEnum messageType;

    /** 消息内容 */
    private String content;

    /** 创建时间（数据库默认 CURRENT_TIMESTAMP，无需手动设置） */
    private LocalDateTime createdTime;

    /** 更新时间（用于同步Redis和MySQL,数据库默认 ON UPDATE CURRENT_TIMESTAMP，无需手动设置） */
    private LocalDateTime updatedTime;
}
