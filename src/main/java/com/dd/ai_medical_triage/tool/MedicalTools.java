package com.dd.ai_medical_triage.tool;

import org.springframework.ai.tool.annotation.Tool;
import org.springframework.stereotype.Component;


@Component
public class MedicalTools {
    @Tool(description = "查询患者所需要挂号的对应科室")
    public String queryDepartment(String patientName) {

        // tODO 查询患者所需要挂号的对应科室
        return "科室名称";
    }

    @Tool(description = "查询患者所需要挂号的对应医生")
    public String queryDoctor(String patientName) {

        // tODO 查询患者所需要挂号的对应医生
        return "医生名称";
    }

    @Tool(description = "查询当前科室医生的出诊情况")
    public String queryDoctorSchedule(String departmentName) {

        // tODO 查询当前科室医生的出诊情况
        return "出诊情况";
    }
}
