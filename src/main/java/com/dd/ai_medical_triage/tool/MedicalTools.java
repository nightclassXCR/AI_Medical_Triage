package com.dd.ai_medical_triage.tool;

import com.dd.ai_medical_triage.service.base.DepartmentService;
import com.dd.ai_medical_triage.service.base.DoctorService;
import com.dd.ai_medical_triage.vo.ResultVO;
import lombok.ToString;
import org.springframework.ai.tool.annotation.Tool;
import org.springframework.ai.tool.annotation.ToolParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;


@Component
public class MedicalTools {

    @Autowired
    private DepartmentService departmentService;
    @Autowired
    private DoctorService doctorService;

    @Tool(description = "获取医院全部科室")
    public ResultVO queryDepartment(){

        List<String> departmentNames=departmentService.getAllDepartmentNames();
        return ResultVO.success(departmentNames);
    }

    @Tool(description = "查询科室的医生")
    public ResultVO queryDoctor(@ToolParam(description = "科室名称") String departmentName) {

        return ResultVO.success(doctorService.queryDoctorNameByDepartmentName(departmentName));
    }

    @Tool(description = "查询当前科室医生的出诊情况")
    public ResultVO queryDoctorSchedule(@ToolParam(description = "科室名称")String departmentName) {

        return ResultVO.success(doctorService.queryDoctorStatusByDepartmentName(departmentName));


    }
}
