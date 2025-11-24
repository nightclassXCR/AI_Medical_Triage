package com.dd.ai_medical_triage.service.base;


import com.dd.ai_medical_triage.entity.Appointment;
import com.dd.ai_medical_triage.entity.Doctor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public interface DoctorService extends BaseService<Doctor>{
    List<String> queryDoctorNameByDepartmentName(String departmentName);
    List<Map<String,Integer>> queryDoctorStatusByDepartmentName(String departmentName);

    List<Appointment> queryAppointmentByDoctorId(int id);

    String queryDoctorNameById(int id);
}
