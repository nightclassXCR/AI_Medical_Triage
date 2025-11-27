package com.dd.ai_medical_triage.dao.repository;

import cn.hutool.core.util.IdUtil;
import com.dd.ai_medical_triage.dao.mapper.ChatMessageMapper;
import com.dd.ai_medical_triage.dao.mapper.ChatSessionMapper;
import com.dd.ai_medical_triage.entity.ChatMessage;
import com.dd.ai_medical_triage.enums.SimpleEnum.ChatMessageTypeEnum;
import org.springframework.ai.chat.memory.ChatMemoryRepository;
import org.springframework.ai.chat.messages.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * MySQL + Redis 双层缓存实现的会话记忆仓库
 * 优先从Redis读取，未命中则从MySQL加载并同步到Redis；写入时同时更新两者
 */
@Repository
public class DoubleLayerChatMemoryRepository implements ChatMemoryRepository {

    @Autowired
    private RedisChatMemoryRepositoryDialect redisDialect;

    @Autowired
    private ChatMessageMapper chatMessageMapper;

    @Autowired
    private ChatSessionMapper chatSessionMapper;

    /**
     * 获取会话ID列表
     * 若Redis缓存为空则从MySQL加载并同步到Redis
     */
    @NonNull
    @Override
    public List<String> findConversationIds() {
        // 从Redis获取会话ID，若为空则从MySQL加载并同步到Redis
        List<String> redisIds = redisDialect.findConversationIds();
        if (!redisIds.isEmpty()) {
            return redisIds;
        }
        List<String> mysqlIds = chatSessionMapper.selectDistinctSessionIds();
        // 同步到Redis
        mysqlIds.forEach(id -> redisDialect.saveAll(id, findByConversationId(id)));
        return mysqlIds;
    }

    /**
     * 根据会话ID获取消息列表
     * 若Redis缓存为空则从MySQL加载并同步到Redis
     */
    @NonNull
    @Override
    public List<Message> findByConversationId(@NonNull String conversationId) {
        // 1. 先查Redis缓存
        List<Message> redisMessages = redisDialect.findByConversationId(conversationId);
        if (!redisMessages.isEmpty()) {
            return redisMessages;
        }

        // 2. Redis未命中，查MySQL
        List<ChatMessage> poList = chatMessageMapper.selectBySessionId(conversationId);
        if (poList.isEmpty()) {
            return List.of();
        }

        // 3. 转换为Message并同步到Redis
        List<Message> messages = poList.stream()
                .map(this::convertToMessage)
                .toList();
        redisDialect.saveAll(conversationId, messages);
        return messages;
    }

    /**
     * 保存会话消息
     * 同时同步到MySQL和Redis
     */
    @Override
    public void saveAll(@NonNull String conversationId, @NonNull List<Message> messages) {
        if (messages.isEmpty()) {
            return;
        }
        // 1. 先更新MySQL（持久化）
        List<ChatMessage> poList = messages.stream()
                .map(msg -> convertToChatMemory(conversationId, msg))
                .toList();
        chatMessageMapper.batchInsert(poList);

        // 2. 再更新Redis（缓存）
        redisDialect.saveAll(conversationId, messages);
    }

    /**
     * 删除会话消息
     * 同时同步到MySQL和Redis
     */
    @Override
    public void deleteByConversationId(@NonNull String conversationId) {
        // 1. 先删除MySQL数据
        chatMessageMapper.deleteBySessionId(conversationId);
        // 2. 再删除Redis缓存
        redisDialect.deleteByConversationId(conversationId);
    }

    /**
     * ChatMemory转Message
     */
    private Message convertToMessage(ChatMessage memory) {
        Map<String, Object> metadata = new HashMap<>();
        metadata.put("timestamp", memory.getCreatedTime().toString());
        // 其他元数据转换

        return switch (memory.getMessageType()) {
            case USER-> UserMessage.builder()
                    .text(memory.getContent())
                    .metadata(metadata)
                    .build();
            case ASSISTANT -> new AssistantMessage(memory.getContent(), metadata);
            case SYSTEM -> SystemMessage.builder()
                    .text(memory.getContent())
                    .metadata(metadata)
                    .build();
            case TOOL -> new ToolResponseMessage(List.of(), metadata);
            default -> throw new IllegalArgumentException("未知消息类型: " + memory.getMessageType());
        };
    }

    /**
     * Message转ChatMemory
     */
    private ChatMessage convertToChatMemory(String conversationId, Message message) {
        return ChatMessage.builder()
                .chatMessageId(IdUtil.getSnowflakeNextId()) // 雪花ID
                .chatSessionId(conversationId)
                .content(message.getText())
                .messageType(ChatMessageTypeEnum.fromValue(message.getMessageType().getValue()))
                .createdTime(LocalDateTime.now())
                .build();
    }
}
