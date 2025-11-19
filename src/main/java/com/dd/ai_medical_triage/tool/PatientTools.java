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


    @Tool(description = "记录患者的个人信息")
    public ResultVO recordPatientInfo(@ToolParam(description = "患者姓名") String patientName,
                                      @ToolParam(description = "患者年龄") String age,
                                      @ToolParam (description = "患者性别") String gender,
                                      @ToolParam (description = "患者电话号码") String phoneNumber,
                                      @ToolParam (description = "患者身份证号") String idCard,
                                      @ToolParam (description = "患者身高") String height,
                                      @ToolParam (description = "患者体重") String weight,
                                      @ToolParam (description = "患者病历号（如果没有可以先不记录，会自动生成一个病历号，生成之后告诉病人）") String caseNumber) {
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
