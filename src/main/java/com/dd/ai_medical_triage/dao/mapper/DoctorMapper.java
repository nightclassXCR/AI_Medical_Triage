package com.dd.ai_medical_triage.dao.mapper;


import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.dd.ai_medical_triage.entity.Appointment;
import com.dd.ai_medical_triage.entity.Doctor;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;
import java.util.Map;

@Mapper
public interface DoctorMapper extends BaseMapper<Doctor> {
    @Select("select name from doctor where department_id=#{departmentId}")
    List<String> queryDoctorNameByDepartmentId(int departmentId);

    @Select("select name,status from doctor where department_id= #{id}")
    List<Map<String, Integer>> queryDoctorStatusByDepartmentId(int id);

    @Select("select * from appointment where doctor_id=#{id}")
    List<Appointment> queryAppointmentByDoctorId(int id);

    @Select("select name from doctor where doctor_id= #{id}")
    String queryDoctorNameById(int id);
}
