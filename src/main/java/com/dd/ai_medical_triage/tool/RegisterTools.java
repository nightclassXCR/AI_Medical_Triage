package com.dd.ai_medical_triage.tool;



import com.dd.ai_medical_triage.dto.appointment.AppointmentRequestDTO;
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

    @Tool(description = "【挂号执行工具】当且仅当所有必要信息（患者病历号、医生ID、日期、时间段）都已确认后，用于**执行最终的预约挂号操作**。这是一个**写入数据库**的关键操作。")
    public ResultVO appointmentFunction(@ToolParam(description = """
        包含所有挂号详情的JSON对象，是执行挂号的**最终输入**。
        
        【必须字段列表】:
        1. patientId (long): 预约患者的**唯一数字ID**。
        2. doctorId (long): 目标医生的**唯一数字ID**。
        3. scheduleId (long): 目标时间段的**排班数字ID**。
        4. appointmentTime (string): 最终预约的**时间戳**，格式必须为 'YYYY-MM-DD HH:mm'。
           
        【调用前置条件】：
        - 必须先通过其他工具获取到 patientId 和 scheduleId。
        - 必须确保所有ID都是**数字类型**。
        """)AppointmentRequestDTO request) {
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
