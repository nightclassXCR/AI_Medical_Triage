package com.dd.ai_medical_triage.tool;


import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.dd.ai_medical_triage.entity.Patient;
import com.dd.ai_medical_triage.enums.SimpleEnum.GenderEnum;
import com.dd.ai_medical_triage.service.base.PatientService;
import com.dd.ai_medical_triage.vo.ResultVO;
import org.springframework.ai.tool.annotation.Tool;
import org.springframework.ai.tool.annotation.ToolParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;


import java.time.LocalDateTime;

@Component
public class PatientTools {
    @Autowired
    private PatientService patientService;

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
     * @param caseNumber   患者病历号
     * @return 患者信息
     */
    @Tool(description = "【患者档案创建与病历号生成工具】**如果患者是首次就诊或没有病历号，必须调用此工具**。该工具用于收集患者基本信息并**创建新的患者档案**。成功调用后，系统将自动生成一个唯一的病历号，必须将此新病历号告知患者。")
    public ResultVO<?> recordPatientInfo(
            @ToolParam(description = "患者的**全名**，不能为空。") String patientName,
            @ToolParam(description = "患者的**年龄**，请尝试获取整数值。") String age,
            @ToolParam (description = "患者的**性别**，必须为明确的字符串（例如 MALE, FEMALE）。") String gender,
            @ToolParam (description = "患者的**电话号码**，用于联系。") String phoneNumber,
            @ToolParam (description = "患者的**身份证号**，可选。") String idCard,
            @ToolParam (description = "患者的**身高**，可选（例如：175cm）。") String height,
            @ToolParam (description = "患者的**体重**，可选（例如：65kg）。") String weight,
            @ToolParam (description = "【可选/忽略】患者的病历号。**如果患者没有提供，请忽略此参数（留空）**，系统将自动生成病历号。") String caseNumber) {
        if (StringUtils.isBlank(patientName)) {
            return ResultVO.fail("患者姓名不能为空");
        }
        Patient patient = new Patient();
        patient.setName(patientName);
        patient.setAge(age);
        patient.setGender(GenderEnum.valueOf(gender));
        patient.setPhoneNumber(phoneNumber);
        patient.setIdCard(idCard);
        patient.setHeight(height);
        patient.setWeight(weight);
        if (StringUtils.isBlank(caseNumber)) {
            // 生成病历号，避免idCard为null的情况
            String suffix = (idCard != null && idCard.length() >= 4) ?
                    idCard.substring(idCard.length() - 4) : "0000";
            caseNumber = "C" + System.currentTimeMillis() + suffix;
        }
        patient.setCaseNumber(caseNumber);
        patient.setCreateTime(LocalDateTime.now());
        return ResultVO.success(patientService.save(patient));
    }
}
