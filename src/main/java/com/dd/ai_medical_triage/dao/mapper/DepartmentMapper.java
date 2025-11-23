package com.dd.ai_medical_triage.dao.mapper;


import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.dd.ai_medical_triage.entity.Department;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface DepartmentMapper extends BaseMapper<Department> {
    @Select("select department_id from department where name = #{departmentName}")
    int getIdByName(String departmentName);
}
