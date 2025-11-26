package com.dd.ai_medical_triage.dto.chat;

import com.dd.ai_medical_triage.dto.PageParam;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 会话查询参数DTO
 */
@NoArgsConstructor
@Data
public class ChatSessionQueryDTO extends PageParam {

    /** 用户ID */
    private Long userId;
}
