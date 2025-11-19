package com.dd.ai_medical_triage.service.impl;


import com.dd.ai_medical_triage.entity.Doctor;
import com.dd.ai_medical_triage.mapper.DoctorMapper;
import com.dd.ai_medical_triage.service.base.DoctorService;
import org.springframework.stereotype.Service;

@Service
public class DoctorServiceImpl extends BaseServiceImpl<DoctorMapper, Doctor> implements DoctorService {
}
