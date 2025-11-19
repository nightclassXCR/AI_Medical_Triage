package com.dd.ai_medical_triage.service.impl;

import com.dd.ai_medical_triage.entity.Session;
import com.dd.ai_medical_triage.mapper.SessionMapper;
import com.dd.ai_medical_triage.service.base.SessionService;
import org.springframework.stereotype.Service;

@Service
public class SessionServiceImpl extends BaseServiceImpl<SessionMapper, Session> implements SessionService {
}
