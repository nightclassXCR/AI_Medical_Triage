package com.dd.ai_medical_triage.dao.repository;

import com.alibaba.fastjson.JSON;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.chat.messages.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.time.Instant;
import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Component
public class RedisChatMemoryRepositoryDialect {

    @Autowired
    private RedisTemplate<String, Object> redisTemplate;

    @Autowired
    private ObjectMapper objectMapper;

    // Redis里存所有活跃会话ID的Set key，方便查找所有会话
    private static final String CACHE_SESSION_KEY = "chat:session_ids";
    // 每个会话消息列表的key前缀
    private static final String CACHE_MESSAGE_LIST_PREFIX = "chat:messages:";

    /**
     * 获取所有活跃会话ID
     * Redis数据结构：Set（无序且唯一）
     * 用于快速获取当前所有存在的会话ID
     */
    public List<String> findConversationIds() {
        Set<Object> members = redisTemplate.opsForSet().members(CACHE_SESSION_KEY);
        return Optional.ofNullable(members)
                .filter(m -> !m.isEmpty())
                .map(m -> m.stream().map(Object::toString).collect(Collectors.toList()))
                .orElse(Collections.emptyList());
    }

    /**
     * 根据会话ID获取该会话的所有消息列表（多轮对话历史）反序列化
     * Redis数据结构：List（有序）
     * 按消息顺序返回，方便构造对话上下文
     */
    public List<Message> findByConversationId(String conversationId) {
        String key = CACHE_MESSAGE_LIST_PREFIX + conversationId;
        Long size = redisTemplate.opsForList().size(key);
        if(size == null || size == 0L){
            return Collections.emptyList();
        }
        List<Object> range = redisTemplate.opsForList().range(key, size-21, -1);
        List<Message> messages = new ArrayList<>();
        for(Object o:range){
            String json = JSON.toJSONString(o);
            try {
                // 从 JsonParser 中读取 JSON 数据，并将其反序列化为 JsonNode（树形结构）对象
                JsonNode jsonNode = objectMapper.readTree(json);
                messages.add(getMessage(jsonNode));
            } catch (JsonProcessingException e) {
                throw new RuntimeException("Error deserializing message", e);
            }
        }
        return messages;
    }

    /**
     * 保存一批消息到指定会话中，追加到消息列表末尾
     * Redis数据结构：List（右侧追加）
     * 并且保证会话ID存在于会话ID集合中
     */
    public void saveAll(String conversationId, List<Message> messages) {
        if(CollectionUtils.isEmpty(messages)) return;
        String key=CACHE_MESSAGE_LIST_PREFIX+conversationId;
        deleteByConversationId(conversationId);
        redisTemplate.opsForSet().add(CACHE_SESSION_KEY, conversationId);
        List<Message> filteredMessages = messages.stream()
                .filter(Objects::nonNull)
                .filter(m -> m.getText() != null && m.getMessageType() != null).toList();
        List<Message> finalMessages = new ArrayList<>();
        for(Message message:filteredMessages){
            String json = JSON.toJSONString(message);
            try {
                JsonNode jsonNode = objectMapper.readTree(json);
                finalMessages.add(getMessageWithTime(jsonNode,message.getMessageType(),message.getText()));
            } catch (JsonProcessingException e) {
                throw new RuntimeException(e);
            }
        }
        redisTemplate.opsForList().rightPushAll(key, finalMessages.toArray());
        int maxHistorySize = 100;
        redisTemplate.opsForList().trim(key, -maxHistorySize, -1);
    }

    /**
     * 删除指定会话的所有消息以及会话ID集合中的对应ID
     * Redis数据结构：删除List + Set中元素
     */
    public void deleteByConversationId(String conversationId) {
        String key = CACHE_MESSAGE_LIST_PREFIX + conversationId;
        redisTemplate.delete(key);
        redisTemplate.opsForSet().remove(CACHE_SESSION_KEY, conversationId);
    }

    /**
     * 在saveall操作时统一添加系统时间
     * @param jsonNode 待转换的 JsonNode
     * @param messageType 消息类型
     * @param textContent 消息内容
     * @return Message 实例
     */
    private Message getMessageWithTime(JsonNode jsonNode,MessageType messageType,String textContent){
        // 从 jsonNode 中获取 metadata 字段并转换为 Map<String, Object>
        Map<String, Object> metadata = Optional.ofNullable(jsonNode)
                .map(node -> node.get("metadata"))
                .map(node -> objectMapper.convertValue(
                        node, new TypeReference<Map<String, Object>>() {}))
                .orElse(new HashMap<>());
        if(!metadata.containsKey("timestamp")){
            metadata.put("timestamp", Instant.now().toString());
        }
        // 根据不同的消息类型，构造对应的 Message 子类实例并返回
        return switch (messageType) {
            case ASSISTANT -> new AssistantMessage(textContent, metadata);          // 助手消息
            case USER -> UserMessage.builder().text(textContent).metadata(metadata).build();   // 用户消息
            case SYSTEM -> SystemMessage.builder().text(textContent).metadata(metadata).build(); // 系统消息
            case TOOL -> new ToolResponseMessage(List.of(), metadata);                // 工具调用消息
        };
    }

    /**
     * 将一个 JsonNode 转换成对应的 Message 子类实例。
     * 根据 messageType 字段决定返回哪种 Message 类型，并提取 text 和 metadata 字段。
     * 额外会在 metadata 中添加当前时间戳。
     *
     * @param jsonNode 传入的 JSON 树节点，包含 messageType、text、metadata 等字段
     * @return 对应类型的 Message 对象实例（AssistantMessage、UserMessage、SystemMessage 或 ToolResponseMessage）
     */
    private Message getMessage(JsonNode jsonNode) {
        // 从 jsonNode 中获取 messageType 字段的文本内容，默认为 USER 类型
        String type = Optional.ofNullable(jsonNode)
                .map(node -> node.get("messageType"))  // 取 messageType 字段节点
                .map(JsonNode::asText)                 // 转为字符串
                .orElse(MessageType.USER.getValue()); // 如果没有该字段，默认是 USER 类型

        // 根据字符串转换为枚举类型 MessageType
        MessageType messageType = MessageType.valueOf(type.toUpperCase());

        // 从 jsonNode 中获取 text 字段的内容
        String textContent = Optional.ofNullable(jsonNode)
                .map(node -> node.get("text"))    // 取 text 字段节点
                .map(JsonNode::asText)            // 转为字符串
                // 如果 text 字段不存在，根据消息类型返回默认值：
                // SYSTEM 和 USER 类型默认返回空字符串 ""，其他类型返回 null
                .orElseGet(() ->
                        (messageType == MessageType.SYSTEM || messageType == MessageType.USER)
                                ? ""
                                : null);

        // 从 jsonNode 中获取 metadata 字段并转换为 Map<String, Object>
        Map<String, Object> metadata = Optional.ofNullable(jsonNode)
                .map(node -> node.get("metadata"))       // 取 metadata 节点
                .map(node -> objectMapper.convertValue( // 用 Jackson ObjectMapper 转换成 Map
                        node, new TypeReference<Map<String, Object>>() {}))
                .orElse(new HashMap<>());                 // 如果没有 metadata 字段，返回空 Map

        // 在 metadata 中加入当前时间戳，key 是 "timestamp"，值是当前 ISO 格式时间字符串
        if(!metadata.containsKey("timestamp")){
            metadata.put("timestamp", Instant.now().toString());
        }

        // 根据不同的消息类型，构造对应的 Message 子类实例并返回
        return switch (messageType) {
            case ASSISTANT -> new AssistantMessage(textContent, metadata);          // 助手消息
            case USER -> UserMessage.builder().text(textContent).metadata(metadata).build();   // 用户消息
            case SYSTEM -> SystemMessage.builder().text(textContent).metadata(metadata).build(); // 系统消息
            case TOOL -> new ToolResponseMessage(List.of(), metadata);                // 工具调用消息
        };
    }
}

