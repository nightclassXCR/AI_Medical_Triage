package com.dd.ai_medical_triage.service.base;

import com.dd.ai_medical_triage.entity.Department;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface DepartmentService extends BaseService<Department>{
    public List<String> getAllDepartmentNames();
}
