package com.dd.ai_medical_triage.tool;


import com.dd.ai_medical_triage.dto.request.RegisterAppointmentRequest;
import com.dd.ai_medical_triage.entity.Appointment;
import com.dd.ai_medical_triage.service.base.AppointmentService;
import com.dd.ai_medical_triage.vo.ResultVO;
import org.springframework.ai.tool.annotation.Tool;
import org.springframework.ai.tool.annotation.ToolParam;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class RegisterTools {

    @Autowired
    private RabbitTemplate rabbitTemplate;

    @Autowired
    private AppointmentService appointmentService;
    // todo
//    @Tool(description = "为患者挂号并发送消息给医生端")
//    public ResultVO<Appointment> register(RegisterAppointmentRequest req) {
//        return ResultVO.success(appointmentService.createAppointment(req));
//    }
}
