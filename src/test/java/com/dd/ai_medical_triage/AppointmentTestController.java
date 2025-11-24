package com.dd.ai_medical_triage;

import com.dd.ai_medical_triage.dto.tool.AppointmentRequestDTO;
import com.dd.ai_medical_triage.service.base.AppointmentService;
import com.dd.ai_medical_triage.vo.ResultVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/test/appointment")
public class AppointmentTestController {

    @Autowired
    private AppointmentService appointmentService;

    @PostMapping("/register")
    public ResultVO testRegister(@RequestBody AppointmentRequestDTO dto) {
        return appointmentService.register(dto);
    }
}
