package com.dd.ai_medical_triage.service.impl;

import com.dd.ai_medical_triage.entity.Department;
import com.dd.ai_medical_triage.dao.mapper.DepartmentMapper;
import com.dd.ai_medical_triage.service.base.DepartmentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DepartmentServiceImpl extends BaseServiceImpl<DepartmentMapper, Department> implements DepartmentService {

     @Autowired
     private DepartmentMapper departmentMapper;

     @Override
      public List<String> getAllDepartmentNames() {
    	 return departmentMapper.selectList(null).stream()
                 .map(Department::getName)
                 .collect(java.util.stream.Collectors.toList());

     }


}
