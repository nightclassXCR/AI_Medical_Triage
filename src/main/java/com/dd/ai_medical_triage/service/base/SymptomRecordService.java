package com.dd.ai_medical_triage.service.base;

import com.dd.ai_medical_triage.entity.SymptomRecord;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface SymptomRecordService extends BaseService<SymptomRecord>{

    public void addSymptom(int patientId, String json);

    public List<String> getSymptoms(int sessionId);

    public void clear(int sessionId);
}
