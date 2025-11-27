package com.dd.ai_medical_triage.service.impl;

import com.dd.ai_medical_triage.convert.ChatMessageConvert;
import com.dd.ai_medical_triage.dao.mapper.ChatMessageMapper;
import com.dd.ai_medical_triage.dto.chat.ChatMessageDetailDTO;
import com.dd.ai_medical_triage.entity.ChatMessage;
import com.dd.ai_medical_triage.enums.ErrorCode.ErrorCode;
import com.dd.ai_medical_triage.enums.SimpleEnum.ChatMessageTypeEnum;
import com.dd.ai_medical_triage.exception.BusinessException;
import com.dd.ai_medical_triage.service.base.ChatMessageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

/**
 * AI会话记录模块 Service 实现类
 */
@Service
public class ChatMessageServiceImpl extends BaseServiceImpl<ChatMessageMapper, ChatMessage> implements ChatMessageService {

    @Autowired
    private ChatMessageMapper chatMessageMapper;

    @Autowired
    private ChatMessageConvert chatMessageConvert;

    /**
     * 根据会话ID获取会话记录
     * @param sessionId 会话ID
     * @return 会话记录列表
     */
    @Override
    public List<ChatMessageDetailDTO> getBySessionId(String sessionId) {
        return chatMessageConvert.chatMessageListToChatMessageDetailDTOList(chatMessageMapper.selectBySessionId(sessionId));
    }

    /**
     * 插入用户的第一条prompt
     * @param sessionId 会话ID
     * @param prompt 提示语
     * @return 插入结果（true成功/false失败）
     */
    @Override
    public Boolean insertFirstPrompt(String sessionId, String prompt) {
        // 1. 创建会话记录
        ChatMessage chatMessage = new ChatMessage();
        chatMessage.setChatSessionId(sessionId);
        chatMessage.setMessageType(ChatMessageTypeEnum.USER);
        chatMessage.setContent(prompt);
        chatMessage.setCreateTime(LocalDateTime.now());
        chatMessage.setUpdateTime(LocalDateTime.now());

        // 2. 插入会话记录
        if(chatMessageMapper.insert(chatMessage) <= 0) {
           throw new BusinessException(ErrorCode.DATA_INSERT_FAILED);
        }

        return true;
    }

    /**
     * 根据会话ID删除会话记录
     * @param sessionId 会话ID
     * @return 删除结果（true成功/false失败）
     */
    @Override
    public boolean removeBySessionId(String sessionId) {
        return chatMessageMapper.deleteBySessionId(sessionId) > 0;
    }
}
