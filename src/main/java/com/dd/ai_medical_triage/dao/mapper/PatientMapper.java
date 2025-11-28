package com.dd.ai_medical_triage.dao.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.dd.ai_medical_triage.entity.Patient;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface PatientMapper extends BaseMapper<Patient> {

    @Select("select * from patient where name = #{name}")
    Patient getByName(String name);
}
