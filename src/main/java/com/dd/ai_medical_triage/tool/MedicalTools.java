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
    @Tool(description = "用于挂号场景。严格获取数据库中所有科室的名称列表。输出：为一个包含所有科室名称的JSON字符串列表，例如 ['内科', '外科', '骨科']。")
    public ResultVO<List<String>> queryDepartment(){

        List<String> departmentNames=departmentService.getAllDepartmentNames();
        return ResultVO.success(departmentNames);
    }

    /**
     * 查询科室的医生
     * @param departmentName 科室名称
     * @return 医生列表
     */
    @Tool(description = "根据已知的科室名称，获取该科室下所有医生的姓名列表。如果科室名称未知，不得调用此工具。输出为一个包含医生姓名的JSON字符串列表，例如 ['张医生', '李医生']。")
    public ResultVO<List<String>> queryDoctor(@ToolParam(description = "科室名称") String departmentName) {
        return ResultVO.success(doctorService.queryDoctorNameByDepartmentName(departmentName));
    }

    /**
     * 查询当前科室的医生出诊情况
     * @param departmentName 科室名称
     * @return 医生出诊情况列表
     */
    @Tool(description = "查询某一科室下，所有医生的出诊状态信息。输出为一个JSON列表，其中每个元素是一个Map，包含医生信息和其当前的出诊状态（例如：{doctorName: '王医生', status: 1}）。")
    public ResultVO<List<Map<String,Integer>>> queryDoctorSchedule(@ToolParam(description = "科室名称")String departmentName) {

        return ResultVO.success(doctorService.queryDoctorStatusByDepartmentName(departmentName));

    }

    @Tool(description = "根据已知的医生ID，查询该医生的详细挂号和排班情况。仅在用户明确提供或工具链中获取到医生ID时使用。输出为挂号信息JSON对象。")
    public ResultVO queryDoctorScheduleById(@ToolParam(description = "医生id")int id) {

        return ResultVO.success(doctorService.queryAppointmentByDoctorId(id));

    }

    @Tool(description = "根据已知的医生ID，查询该医生的全名。主要用于将ID映射回医生姓名以提供给用户。输出为医生姓名的字符串。")
    public ResultVO queryDoctorNameById(@ToolParam(description = "医生id")int id) {

        return ResultVO.success(doctorService.queryDoctorNameById(id));

    }

}
