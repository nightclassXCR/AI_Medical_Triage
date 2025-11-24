package com.dd.ai_medical_triage.service.impl;

import com.dd.ai_medical_triage.entity.ChatRecord;
import com.dd.ai_medical_triage.dao.mapper.ChatRecordMapper;
import com.dd.ai_medical_triage.service.base.ChatRecordService;
import org.springframework.stereotype.Service;


@Service
public class ChatRecordServiceImpl extends BaseServiceImpl<ChatRecordMapper, ChatRecord> implements ChatRecordService {
}
