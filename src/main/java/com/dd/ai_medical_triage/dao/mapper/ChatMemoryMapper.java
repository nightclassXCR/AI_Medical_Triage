package com.dd.ai_medical_triage.dao.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.dd.ai_medical_triage.entity.ChatMemory;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * AI会话内存模块Mapper接口，对应chat_memory表操作
 */
@Mapper
public interface ChatMemoryMapper extends BaseMapper<ChatMemory> {

    /**
     * 批量插入会话内存
     */
    void batchInsert(@Param("list") List<ChatMemory> list);

    /**
     * 根据会话ID查询所有消息（按创建时间升序）
     */
    @Select("SELECT * FROM chat_memory WHERE session_id = #{sessionId} ORDER BY created_time ASC")
    List<ChatMemory> selectBySessionId(@Param("sessionId") String sessionId);

    /**
     * 查询所有会话ID
     */
    @Select("SELECT DISTINCT session_id FROM chat_memory")
    List<String> selectDistinctSessionIds();

    /**
     * 根据会话ID删除所有记忆
     */
    @Delete("DELETE FROM chat_memory WHERE session_id = #{sessionId}")
    int deleteBySessionId(@Param("sessionId") String sessionId);

}
