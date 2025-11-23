package com.dd.ai_medical_triage.service.impl;

import com.dd.ai_medical_triage.entity.Session;
import com.dd.ai_medical_triage.mapper.SessionMapper;
import com.dd.ai_medical_triage.service.base.SessionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.util.HashMap;
import java.util.Map;

import static com.dd.ai_medical_triage.utils.constants.RedisConstants.SESSION_KEY;

@Service
public class SessionServiceImpl extends BaseServiceImpl<SessionMapper, Session> implements SessionService {

    @Autowired
    private StringRedisTemplate redisTemplate;

    @Override
    public void createSession(int sessionId, int patientId){
        Map<String, String> map = new HashMap<>();
        map.put("patientId", String.valueOf(patientId));
        map.put("state", "diagnosing");

        redisTemplate.opsForHash().putAll(SESSION_KEY + sessionId, map);
        redisTemplate.expire(SESSION_KEY + sessionId, Duration.ofMinutes(30));
    }

    @Override
    public void updateContext(int sessionId, String content) {
        redisTemplate.opsForHash().put(SESSION_KEY + sessionId, "buffer", content);
    }

    @Override
    public String getContext(int sessionId) {
        return (String) redisTemplate.opsForHash().get(SESSION_KEY + sessionId, "buffer");
    }
}
