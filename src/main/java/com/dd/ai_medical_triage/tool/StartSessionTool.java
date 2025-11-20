package com.dd.ai_medical_triage.tool;


import com.dd.ai_medical_triage.entity.Session;
import com.dd.ai_medical_triage.service.base.SessionService;
import com.dd.ai_medical_triage.vo.ResultVO;
import org.springframework.ai.tool.annotation.Tool;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
public class StartSessionTool {

    @Autowired
    private SessionService sessionService;

    @Tool(description = "开始新的AI问诊会话")
    public ResultVO startSession() {
        Session session = new Session();
        session.setPatientId(1);
        session.setStatus("started");
        session.setCreatedTime(LocalDateTime.now());
        return ResultVO.success(sessionService.save(session));
    }


}
