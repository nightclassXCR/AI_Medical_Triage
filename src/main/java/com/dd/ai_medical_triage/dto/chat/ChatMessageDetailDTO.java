package com.dd.ai_medical_triage.dto.chat;

import com.dd.ai_medical_triage.enums.SimpleEnum.ChatMessageTypeEnum;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * 会话消息详情DTO
 */
@NoArgsConstructor
@Data
public class ChatMessageDetailDTO {

    /** 会话ID */
    private String chatSessionId;

    /** 消息角色 */
    private ChatMessageTypeEnum messageType;

    /** 消息内容 */
    private String content;

    /** 创建时间（数据库默认 CURRENT_TIMESTAMP，无需手动设置） */
    private LocalDateTime createdTime;
}
