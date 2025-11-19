package com.dd.ai_medical_triage.service.impl;

import com.dd.ai_medical_triage.entity.Department;
import com.dd.ai_medical_triage.mapper.DepartmentMapper;
import com.dd.ai_medical_triage.service.base.DepartmentService;
import org.springframework.stereotype.Service;

@Service
public class DepartmentServiceImpl extends BaseServiceImpl<DepartmentMapper, Department> implements DepartmentService {
}
