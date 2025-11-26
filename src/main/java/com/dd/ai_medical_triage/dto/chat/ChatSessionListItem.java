package com.dd.ai_medical_triage.dto.chat;

import com.baomidou.mybatisplus.annotation.TableId;
import com.dd.ai_medical_triage.dto.PageResult;
import com.dd.ai_medical_triage.enums.SimpleEnum.SessionStatusEnum;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * 会话列表DTO
 */
@NoArgsConstructor
@Data
public class ChatSessionListItem {

    /** 会话ID */
    @TableId(value = "chat_session_id")
    @NotBlank(message = "会话ID不能为空")
    private String chatSessionId;

    /** 用户ID */
    private Long userId;

    /** 会话摘要（根据用户的第一个消息给出） */
    private String summary;

    /** 创建时间*/
    private LocalDateTime createdTime;

    /** 更新时间 */
    private LocalDateTime updatedTime;

    /** 会话状态 */
    private SessionStatusEnum status;
}
