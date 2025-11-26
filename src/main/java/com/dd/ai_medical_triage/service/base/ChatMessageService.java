package com.dd.ai_medical_triage.service.base;

import com.dd.ai_medical_triage.dto.chat.ChatMessageDetailDTO;
import com.dd.ai_medical_triage.entity.ChatMessage;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * AI会话记录模块 Service 接口
 */
@Service
public interface ChatMessageService extends BaseService<ChatMessage> {

    /**
     * 根据会话ID获取会话记录
     * @param sessionId 会话ID
     * @return 会话记录列表
     */
    List<ChatMessageDetailDTO> getBySessionId(String sessionId);

    /**
     * 插入用户的第一条prompt
     * @param sessionId 会话ID
     * @param prompt 提示语
     * @return 插入结果（true成功/false失败）
     */
    Boolean insertFirstPrompt(String sessionId, String prompt);

    /**
     * 根据会话ID删除会话记录
     * @param sessionId 会话ID
     * @return 删除结果（true成功/false失败）
     */
    boolean removeBySessionId(String sessionId);
}
