package com.dd.ai_medical_triage.service.impl;


import com.dd.ai_medical_triage.entity.Doctor;
import com.dd.ai_medical_triage.mapper.DepartmentMapper;
import com.dd.ai_medical_triage.mapper.DoctorMapper;
import com.dd.ai_medical_triage.service.base.DepartmentService;
import com.dd.ai_medical_triage.service.base.DoctorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class DoctorServiceImpl extends BaseServiceImpl<DoctorMapper, Doctor> implements DoctorService {

    @Autowired
    private DoctorMapper doctorMapper;
    @Autowired
    private DepartmentMapper departmentMapper;
    @Override
    public List<String> queryDoctorNameByDepartmentName(String departmentName) {
        int id= departmentMapper.getIdByName(departmentName);

        return doctorMapper.queryDoctorNameByDepartmentId(id);

    }

    @Override
    public List<Map<String, Integer>> queryDoctorStatusByDepartmentName(String departmentName) {
        int id= departmentMapper.getIdByName(departmentName);
        return doctorMapper.queryDoctorStatusByDepartmentId(id);
    }
}
