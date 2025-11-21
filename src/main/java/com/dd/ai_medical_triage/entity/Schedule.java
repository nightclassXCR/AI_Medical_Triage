package com.dd.ai_medical_triage.entity;


import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Schedule {

    private Long scheduleId;
    private Long doctorId;
    private LocalDateTime workDate;
    private Integer timeSlot;
    private Integer quota;
    private Integer soldCount;
    private Integer version;
}
