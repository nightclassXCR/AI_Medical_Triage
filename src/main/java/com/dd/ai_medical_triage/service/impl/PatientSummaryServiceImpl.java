package com.dd.ai_medical_triage.service.impl;

import com.dd.ai_medical_triage.entity.PatientSummary;
import com.dd.ai_medical_triage.mapper.PatientSummaryMapper;
import com.dd.ai_medical_triage.service.base.PatientSummaryService;
import org.springframework.stereotype.Service;

@Service
public class PatientSummaryServiceImpl extends BaseServiceImpl<PatientSummaryMapper, PatientSummary> implements PatientSummaryService {
}
