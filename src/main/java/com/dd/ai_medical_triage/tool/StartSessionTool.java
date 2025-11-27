package com.dd.ai_medical_triage.tool;


import com.dd.ai_medical_triage.entity.ChatSession;
import com.dd.ai_medical_triage.enums.SimpleEnum.SessionStatusEnum;
import com.dd.ai_medical_triage.service.base.ChatSessionService;
import com.dd.ai_medical_triage.vo.ResultVO;
import org.springframework.ai.tool.annotation.Tool;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
public class StartSessionTool {

    @Autowired
    private ChatSessionService chatSessionService;

    /**
     * 开始新的AI问诊会话
     * @return 会话ID
     */
    @Tool(description = "开始新的AI问诊会话")
    public ResultVO<Boolean> startSession() {
        ChatSession chatSession = new ChatSession();
        chatSession.setUserId(1L);
        chatSession.setStatus(SessionStatusEnum.STARTED);
        chatSession.setCreateTime(LocalDateTime.now());
        return ResultVO.success(chatSessionService.save(chatSession));
    }


}
