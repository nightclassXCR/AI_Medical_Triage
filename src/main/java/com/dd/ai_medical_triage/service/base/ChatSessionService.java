package com.dd.ai_medical_triage.service.base;

import com.dd.ai_medical_triage.dto.PageParam;
import com.dd.ai_medical_triage.dto.PageResult;
import com.dd.ai_medical_triage.dto.chat.ChatSessionDetailDTO;
import com.dd.ai_medical_triage.dto.chat.ChatSessionListItem;
import com.dd.ai_medical_triage.dto.chat.ChatSessionQueryDTO;
import com.dd.ai_medical_triage.entity.ChatMessage;
import com.dd.ai_medical_triage.entity.ChatSession;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * AI 会话服务接口（消息->会话）
 */
@Service
public interface ChatSessionService extends BaseService<ChatSession>{

    /**
     * 创建新的会话ID
     * @param userId 用户ID
     * @return 会话记录列表
     */
    String createSessionId(Long userId);

    /**
     * 更新会话摘要
     * @param sessionId 会话ID
     * @param summary 摘要
     * @return 是否成功
     */
    Boolean updateSessionSummary(String sessionId, String summary);

    /**
     * 获取会话内容
     * @param sessionId 会话ID
     * @param userId 用户ID
     * @return 会话内容
     */
    ChatSessionDetailDTO selectSessionById(String sessionId, Long userId);

    /**
     * 查询会话列表数量
     * @param queryDTO 查询参数
     * @return 会话列表
     */
    int countSessions(ChatSessionQueryDTO queryDTO);

    /**
     * 查询会话列表
     * @param queryDTO 查询参数
     * @return 会话列表
     */
    PageResult<ChatSessionListItem> querySessions(ChatSessionQueryDTO queryDTO);
}
