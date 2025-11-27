package com.dd.ai_medical_triage.controller;


import com.dd.ai_medical_triage.dto.tool.AppointmentRequestDTO;
import com.dd.ai_medical_triage.entity.Appointment;
import com.dd.ai_medical_triage.service.base.AppointmentService;
import com.dd.ai_medical_triage.vo.ResultVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController("/api/patient")
public class PatientController {

    @Autowired
    private AppointmentService appointmentService;

    @PutMapping("/payment")

    public ResultVO<Boolean> payment(Appointment request) throws Exception {
        log.info("订单支付");
        boolean result =appointmentService.update(request);

        return ResultVO.success(result);
    }





}
