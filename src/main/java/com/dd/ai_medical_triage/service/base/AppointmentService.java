package com.dd.ai_medical_triage.service.base;


import com.dd.ai_medical_triage.dto.tool.AppointmentRequestDTO;
import com.dd.ai_medical_triage.entity.Appointment;
import com.dd.ai_medical_triage.vo.ResultVO;
import org.springframework.stereotype.Service;

@Service
public interface AppointmentService extends BaseService<Appointment>{

    public ResultVO register(AppointmentRequestDTO appointment);


    boolean update(Appointment request);
}
