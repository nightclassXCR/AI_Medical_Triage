package com.dd.ai_medical_triage.service.base;

import com.dd.ai_medical_triage.entity.MessageQueueLog;
import org.springframework.stereotype.Service;

@Service
public interface MessageQueueLogService {
    boolean insert(MessageQueueLog messageQueueLog);
}
