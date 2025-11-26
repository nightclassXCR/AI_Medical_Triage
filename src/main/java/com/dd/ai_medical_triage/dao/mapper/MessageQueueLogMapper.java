package com.dd.ai_medical_triage.dao.mapper;


import com.dd.ai_medical_triage.entity.MessageQueueLog;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface MessageQueueLogMapper {


    /**
     * 插入消息队列日志
     */
    @Insert("insert into message_queue_log (event_type, payload, status, created_time) values (#{eventType}, #{payload}, #{status}, #{createdTime})")
    int insert(MessageQueueLog messageQueueLog);

}
