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
import java.util.Map;


@Component
public class MedicalTools {

    @Autowired
    private DepartmentService departmentService;
    @Autowired
    private DoctorService doctorService;

    /**
     * 获取全部科室
     * @return 科室对象列表
     */
    @Tool(description = "获取医院全部科室")
    public ResultVO<List<String>> queryDepartment(){

        List<String> departmentNames=departmentService.getAllDepartmentNames();
        return ResultVO.success(departmentNames);
    }

    /**
     * 查询科室的医生
     * @param departmentName 科室名称
     * @return 医生列表
     */
    @Tool(description = "查询科室的医生")
    public ResultVO<List<String>> queryDoctor(@ToolParam(description = "科室名称") String departmentName) {
        return ResultVO.success(doctorService.queryDoctorNameByDepartmentName(departmentName));
    }

    /**
     * 查询当前科室的医生出诊情况
     * @param departmentName 科室名称
     * @return 医生出诊情况列表
     */
    @Tool(description = "查询当前科室医生的出诊情况")
    public ResultVO<List<Map<String,Integer>>> queryDoctorSchedule(@ToolParam(description = "科室名称")String departmentName) {

        return ResultVO.success(doctorService.queryDoctorStatusByDepartmentName(departmentName));

    }

    @Tool(description = "根据医生id查挂号情况")
    public ResultVO queryDoctorScheduleById(@ToolParam(description = "医生id")int id) {

        return ResultVO.success(doctorService.queryAppointmentByDoctorId(id));

    }

    @Tool(description = "根据医生id查姓名")
    public ResultVO queryDoctorNameById(@ToolParam(description = "医生id")int id) {

        return ResultVO.success(doctorService.queryDoctorNameById(id));

    }

}
