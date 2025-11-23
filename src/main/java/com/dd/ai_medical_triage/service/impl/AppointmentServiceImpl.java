package com.dd.ai_medical_triage.service.impl;


import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.dd.ai_medical_triage.annatation.NoRepeatSubmit;
import com.dd.ai_medical_triage.entity.Appointment;
import com.dd.ai_medical_triage.exception.BusinessException;
import com.dd.ai_medical_triage.mapper.AppointmentMapper;
import com.dd.ai_medical_triage.mapper.ScheduleMapper;
import com.dd.ai_medical_triage.service.base.AppointmentService;
import com.dd.ai_medical_triage.vo.ResultVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;



import static com.dd.ai_medical_triage.enums.ErrorCode.ErrorCode.*;

@Service
public class AppointmentServiceImpl extends BaseServiceImpl<AppointmentMapper, Appointment> implements AppointmentService {

    @Autowired
    private ScheduleMapper scheduleMapper;

    /**
     * 供 Spring AI 调用的方法
     * 1. @NoRepeatSubmit: Redis 分布式锁 (防止瞬间重复点击)
     * 2. @Transactional: 事务管理
     */
    @Override
    @NoRepeatSubmit(key = "'lock:reg:' + #req.patientId + ':' + #req.doctorId", lockTime = 10)
    @Transactional(rollbackFor = Exception.class)
    public Long register(Appointment req){
        LambdaQueryWrapper<Appointment> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Appointment::getPatientId, req.getPatientId())
                .eq(Appointment::getDoctorId, req.getDoctorId())
                .eq(Appointment::getAppointmentTime, req.getAppointmentTime())
                .in(Appointment::getStatus, 0, 1); // 0待支付, 1成功 都算已占用

        if (this.count(queryWrapper) > 0) {
            throw new  BusinessException(AI_TIP_NOT_ALLOWED);
        }

        // ================= 2. 原子扣减库存 (防超卖) =================
        // 调用我们在 Mapper 手写的 SQL
        int rows = scheduleMapper.deductStock(req.getScheduleId());
        if (rows <= 0) {
            throw new  BusinessException(AI_TIP_NO_ENOUGH_DOCTOR);
        }

        // ================= 3. 插入挂号记录 (兜底) =================
        Appointment appointment = new Appointment();
        appointment.setPatientId(req.getPatientId());
        appointment.setDoctorId(req.getDoctorId());
        appointment.setScheduleId(req.getScheduleId());
        appointment.setAppointmentTime(req.getAppointmentTime());
        appointment.setStatus(0); // 初始状态：待支付

        try {
            // MP 的 save 方法
            this.save(appointment);
        } catch (DuplicateKeyException e) {
            // 这一步是捕捉数据库唯一索引冲突 (Unique Key)
            // 即使 Redis 锁失效，这里也会拦截住
            throw new BusinessException(AI_TIP_DUPLICATE_REGISTRATION);
        }

        return appointment.getAppointmentId();
    }
}
