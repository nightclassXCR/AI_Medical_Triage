package com.dd.ai_medical_triage.service.impl;

import com.dd.ai_medical_triage.entity.MessageQueueLog;
import com.dd.ai_medical_triage.dao.mapper.MessageQueueLogMapper;
import com.dd.ai_medical_triage.service.base.MessageQueueLogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class MessageQueueLogServiceImpl implements MessageQueueLogService {

    @Autowired
    private MessageQueueLogMapper messageQueueLogMapper;
    @Override
    public boolean insert(MessageQueueLog messageQueueLog) {
        if (messageQueueLogMapper.insert(messageQueueLog) > 0) {
            return true;
        }
        return false;
    }
}
