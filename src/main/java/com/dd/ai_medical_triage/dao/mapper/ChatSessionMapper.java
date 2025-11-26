package com.dd.ai_medical_triage.dao.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.dd.ai_medical_triage.dto.chat.ChatSessionQueryDTO;
import com.dd.ai_medical_triage.entity.ChatSession;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * AI会话内存模块Mapper接口，对应chat_session表操作
 */
@Mapper
public interface ChatSessionMapper extends BaseMapper<ChatSession> {

    /**
     * 根据筛选参数查询会话数量
     * @param queryDTO 筛选参数
     * @return 会话数量
     */
    int countByQuery(ChatSessionQueryDTO queryDTO);

    /**
     * 根据筛选参数查询会话列表
     * @param queryDTO 筛选参数
     * @return 会话列表
     */
    List<ChatSession> selectByQuery(ChatSessionQueryDTO queryDTO);
}
