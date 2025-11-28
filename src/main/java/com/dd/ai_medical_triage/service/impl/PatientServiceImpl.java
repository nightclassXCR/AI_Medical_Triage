package com.dd.ai_medical_triage.service.impl;

import com.dd.ai_medical_triage.entity.Patient;
import com.dd.ai_medical_triage.dao.mapper.PatientMapper;
import com.dd.ai_medical_triage.service.base.PatientService;
import org.springframework.stereotype.Service;


@Service
public class PatientServiceImpl extends BaseServiceImpl<PatientMapper, Patient> implements PatientService {

    @Override
    public Patient getByName(String name){
        return baseMapper.getByName(name);
    }
}
