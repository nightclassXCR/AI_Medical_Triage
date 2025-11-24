package com.dd.ai_medical_triage.dao.repository;

import org.springframework.ai.chat.memory.ChatMemoryRepository;
import org.springframework.ai.chat.messages.Message;

import java.util.List;

public class RedisChatMemoryRepository implements ChatMemoryRepository {

    private final RedisChatMemoryRepositoryDialect dialect;

    public RedisChatMemoryRepository(RedisChatMemoryRepositoryDialect dialect) {
        this.dialect = dialect;
    }

    /**
     * 获取所有的会话ID
     * 用于快速获取当前所有存在的会话ID
     *
     * @return 所有存在会话ID
     */
    @Override
    public List<String> findConversationIds() {
        return dialect.findConversationIds();
    }

    /**
     * 根据会话ID获取该会话的所有消息列表（多轮对话历史）
     *
     * @param conversationId 会话ID
     * @return 该会话的所有消息列表
     */
    @Override
    public List<Message> findByConversationId(String conversationId) {
        return dialect.findByConversationId(conversationId);
    }

    /**
     * 保存指定对话ID对应的消息列表，支持批量保存
     *
     * @param conversationId 会话ID
     * @param messages       要保存的消息列表
     */
    @Override
    public void saveAll(String conversationId, List<Message> messages) {
        dialect.saveAll(conversationId, messages);
    }

    /**
     * 删除指定会话ID对应的所有消息
     *
     * @param conversationId 会话ID
     */
    @Override
    public void deleteByConversationId(String conversationId) {
        dialect.deleteByConversationId(conversationId);
    }
}
