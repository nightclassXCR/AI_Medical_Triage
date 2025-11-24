package com.dd.ai_medical_triage.service.base;

import com.dd.ai_medical_triage.entity.ChatMemory;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * AI会话记录模块 Service 接口
 */
@Service
public interface ChatMemoryService extends BaseService<ChatMemory> {

    /**
     * 根据会话ID获取会话记录
     * @param sessionId 会话ID
     * @return 会话记录列表
     */
    List<ChatMemory> getBySessionId(String sessionId);

    /**
     * 根据会话ID删除会话记录
     * @param sessionId 会话ID
     * @return 删除结果（true成功/false失败）
     */
    boolean removeBySessionId(String sessionId);
}
