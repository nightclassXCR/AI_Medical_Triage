package com.dd.ai_medical_triage.tool;

import com.dd.ai_medical_triage.entity.SymptomRecord;
import com.dd.ai_medical_triage.service.base.SymptomRecordService;
import com.dd.ai_medical_triage.vo.ResultVO;
import org.springframework.ai.tool.annotation.Tool;
import org.springframework.ai.tool.annotation.ToolParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

@Component
public class SymptomExtractTool {

    @Autowired
    private SymptomRecordService symptomRecordService;

    /**
     * 提取患者的症状
     * @param caseNumber 患者病历号
     * @param symptoms   患者症状
     * @param severity   症状的程度
     * @param duration   持续时间
     * @return 症状记录结果
     */
    @Tool(description = "记录患者的症状")
    public ResultVO<Boolean> extractSymptoms(@ToolParam(description = "患者病历号") Long caseNumber,
                                    @ToolParam(description = "患者症状") String symptoms,
                                    @ToolParam(description = "症状的程度") String severity,
                                    @ToolParam(description = "持续时间") String duration) {
        // todo 根据病历号查患者id
        SymptomRecord symptomRecord = new SymptomRecord();
        symptomRecord.setSymptomId(caseNumber);
        symptomRecord.setSymptomText(symptoms);
        symptomRecord.setSeverity(severity);
        symptomRecord.setDuration(duration);
        return ResultVO.success(symptomRecordService.save(symptomRecord));
    }
}
