package com.dd.ai_medical_triage.service.impl;


import com.dd.ai_medical_triage.entity.Appointment;
import com.dd.ai_medical_triage.mapper.AppointmentMapper;
import com.dd.ai_medical_triage.service.base.AppointmentService;
import org.springframework.stereotype.Service;

@Service
public class AppointmentServiceImpl extends BaseServiceImpl<AppointmentMapper, Appointment> implements AppointmentService {
}
