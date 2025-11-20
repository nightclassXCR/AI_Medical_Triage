package com.dd.ai_medical_triage.service.base;

import com.dd.ai_medical_triage.entity.Session;
import org.springframework.stereotype.Service;

@Service
public interface SessionService extends BaseService<Session>{

    public void createSession(int sessionId, int patientId);

    public void updateContext(int sessionId, String content);

    public String getContext(int sessionId);
}
