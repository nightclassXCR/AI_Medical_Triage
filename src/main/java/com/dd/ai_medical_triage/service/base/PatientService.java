package com.dd.ai_medical_triage.service.base;

import com.dd.ai_medical_triage.entity.Patient;
import org.springframework.stereotype.Service;

@Service
public interface PatientService extends BaseService<Patient>{

    Patient getByName(String name);
}
