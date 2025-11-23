package com.dd.ai_medical_triage.service.impl;

import com.dd.ai_medical_triage.entity.SymptomRecord;
import com.dd.ai_medical_triage.mapper.SymptomRecordMapper;
import com.dd.ai_medical_triage.service.base.SymptomRecordService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.util.List;

import static com.dd.ai_medical_triage.utils.constants.RedisConstants.SYMPTOM_KEY;

@Service
public class SymptomRecordServiceImpl extends BaseServiceImpl<SymptomRecordMapper, SymptomRecord> implements SymptomRecordService {
    @Autowired
    private StringRedisTemplate redis;


    @Override
    public void addSymptom(int sessionId, String json) {
        redis.opsForList().rightPush(SYMPTOM_KEY + sessionId, json);
    }

    @Override
    public List<String> getSymptoms(int sessionId) {
        return redis.opsForList().range(SYMPTOM_KEY + sessionId, 0, -1);
    }

    @Override
    public void clear(int sessionId) {
        redis.delete(SYMPTOM_KEY + sessionId);
    }
}
