package com.dd.ai_medical_triage.tool;



import com.dd.ai_medical_triage.dto.tool.AppointmentRequestDTO;
import com.dd.ai_medical_triage.entity.Appointment;
import com.dd.ai_medical_triage.service.base.AppointmentService;
import com.dd.ai_medical_triage.vo.ResultVO;
import org.springframework.ai.tool.annotation.Tool;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.function.Function;

@Component
public class RegisterTools {

    @Autowired
    private RabbitTemplate rabbitTemplate;

    @Autowired
    private AppointmentService appointmentService;

    @Tool(description = "当患者想要挂号的时候，使用这个方法进行挂号")
    public ResultVO appointmentFunction(AppointmentRequestDTO request) {
        try {
            // 【关键点】这里调用的是 Service 代理对象
            // AOP 切面会被触发 -> Redisson 加锁 -> 执行业务
            return appointmentService.register(request);
        } catch (Exception e) {
            // 捕获异常返回给 AI，让 AI 告诉用户为什么失败（例如：请勿重复提交）
            return ResultVO.fail("挂号失败：" + e.getMessage());
        }
    }


}
