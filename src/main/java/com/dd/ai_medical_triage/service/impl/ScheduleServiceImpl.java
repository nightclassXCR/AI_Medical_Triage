package com.dd.ai_medical_triage.service.impl;


import com.dd.ai_medical_triage.entity.Schedule;
import com.dd.ai_medical_triage.mapper.ScheduleMapper;
import com.dd.ai_medical_triage.service.base.ScheduleService;
import org.springframework.stereotype.Service;

@Service
public class ScheduleServiceImpl extends BaseServiceImpl<ScheduleMapper, Schedule> implements ScheduleService {
}
