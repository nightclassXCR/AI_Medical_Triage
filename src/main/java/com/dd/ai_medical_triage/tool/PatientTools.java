package com.dd.ai_medical_triage.tool;


import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.dd.ai_medical_triage.entity.Patient;
import com.dd.ai_medical_triage.enums.SimpleEnum.GenderEnum;
import com.dd.ai_medical_triage.service.base.PatientService;
import com.dd.ai_medical_triage.service.base.UserService;
import com.dd.ai_medical_triage.utils.TokenUtil;
import com.dd.ai_medical_triage.vo.ResultVO;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.ai.tool.annotation.Tool;
import org.springframework.ai.tool.annotation.ToolParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;


import java.time.LocalDateTime;

@Component
public class PatientTools {
    @Autowired
    private PatientService patientService;

    @Autowired
    private UserService userService;


    @Autowired
    private TokenUtil tokenUtil;

    /**
     * 记录患者的个人信息
     *
     * @param patientName  患者姓名
     * @param age          患者年龄
     * @param gender       患者性别
     * @param phoneNumber  患者电话号码
     * @param idCard       患者身份证号
     * @param height       患者身高
     * @param weight       患者体重
     * @return 患者信息
     */
    @Tool(description = "【患者档案创建工具】**如果患者是首次就诊，必须调用此工具**。该工具用于收集患者基本信息并**创建新的患者档案**。")
    public ResultVO<?> recordPatientInfo(
            HttpServletRequest request,
            @ToolParam(description = "患者的**全名**，不能为空。") String patientName,
            @ToolParam(description = "患者的**年龄**，请尝试获取整数值。") String age,
            @ToolParam (description = "患者的**性别**，必须为明确的字符串（例如 MALE, FEMALE）。") String gender,
            @ToolParam (description = "患者的**电话号码**，用于联系。") String phoneNumber,
            @ToolParam (description = "患者的**身份证号**，可选。") String idCard,
            @ToolParam (description = "患者的**身高**，可选（例如：175cm）。") String height,
            @ToolParam (description = "患者的**体重**，可选（例如：65kg）。") String weight
            ) {
        if (StringUtils.isBlank(patientName)) {
            return ResultVO.fail("患者姓名不能为空");
        }

        Patient patient = new Patient();
        // 从请求头中获取token并解析用户ID
        String token = request.getHeader("Authorization");
        if (token != null && token.startsWith("Bearer ")) {
            token = token.substring(7);
            // 假设TokenUtil中有解析用户ID的方法
            Long userId = tokenUtil.getUserIdByToken( token);
            patient.setUserId(userId);
        }
        patient.setName(patientName);
        patient.setAge(age);
        patient.setGender(GenderEnum.valueOf(gender));
        patient.setPhoneNumber(phoneNumber);
        patient.setIdCard(idCard);
        patient.setHeight(height);
        patient.setWeight(weight);

        patient.setCreateTime(LocalDateTime.now());
        return ResultVO.success(patientService.save(patient));
    }

    /**
     * 获取患者的个人信息
     *
     * @param patientName 患者姓名
     * @return 患者信息
     */
    @Tool(description = "【患者信息查询工具】**如果患者已经创建过档案，必须调用此工具**。该工具用于查询患者基本信息。")
    public ResultVO<?> queryPatientInfo(@ToolParam(description = "患者的**全名**，不能为空。") String patientName) {
        return ResultVO.success(patientService.getByName(patientName));
    }
}
