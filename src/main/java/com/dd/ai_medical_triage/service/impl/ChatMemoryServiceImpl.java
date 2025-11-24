package com.dd.ai_medical_triage.service.impl;

import com.dd.ai_medical_triage.dao.mapper.ChatMemoryMapper;
import com.dd.ai_medical_triage.entity.ChatMemory;
import com.dd.ai_medical_triage.service.base.ChatMemoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * AI会话记录模块 Service 实现类
 */
@Service
public class ChatMemoryServiceImpl extends BaseServiceImpl<ChatMemoryMapper, ChatMemory> implements ChatMemoryService {

    @Autowired
    private ChatMemoryMapper chatMemoryMapper;

    /**
     * 根据会话ID获取会话记录
     * @param sessionId 会话ID
     * @return 会话记录列表
     */
    @Override
    public List<ChatMemory> getBySessionId(String sessionId) {
        return chatMemoryMapper.selectBySessionId(sessionId);
    }

    /**
     * 根据会话ID删除会话记录
     * @param sessionId 会话ID
     * @return 删除结果（true成功/false失败）
     */
    @Override
    public boolean removeBySessionId(String sessionId) {
        return chatMemoryMapper.deleteBySessionId(sessionId) > 0;
    }
}
