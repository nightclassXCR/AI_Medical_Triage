package com.dd.ai_medical_triage.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.dd.ai_medical_triage.enums.SimpleEnum.SessionStatusEnum;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
/**
 * AI 会话实体类
 * 存储会话的基本信息
 */

@Data
@AllArgsConstructor
@NoArgsConstructor
@TableName("chat_session")
public class ChatSession {

    /** 会话ID */
    @TableId(value = "chat_session_id")
    private String chatSessionId;

    /** 用户ID */
    private Long userId;

    /** 会话摘要（根据用户的第一个消息给出） */
    private String summary;

    /** 创建时间*/
    private LocalDateTime createTime;

    /** 更新时间 */
    private LocalDateTime updateTime;

    /** 会话状态 */
    private SessionStatusEnum status;
}
