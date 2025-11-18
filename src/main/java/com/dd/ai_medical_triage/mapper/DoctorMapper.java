package com.dd.ai_medical_triage.mapper;


import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.dd.ai_medical_triage.Entity.Doctor;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface DoctorMapper extends BaseMapper<Doctor> {
}
