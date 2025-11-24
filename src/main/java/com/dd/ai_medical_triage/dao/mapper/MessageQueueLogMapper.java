package com.dd.ai_medical_triage.mapper;


import com.dd.ai_medical_triage.entity.MessageQueueLog;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface MessageQueueLogMapper {


    /**
     * 插入消息队列日志
     * @param messageQueueLog
     * @return
     */
    @Insert("insert into message_queue_log (event_type, payload, status, created_time) values (#{messageQueueLog.eventType}, #{messageQueueLog.payload}, #{messageQueueLog.status}, #{messageQueueLog.createdTime})")
    int insert(MessageQueueLog messageQueueLog);
}
