package com.dd.ai_medical_triage.entity;


import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
@TableName("schedule")
public class Schedule {

    @TableId(value = "schedule_id",type = IdType.AUTO)
    private Long scheduleId;
    private Long doctorId;
    private LocalDateTime workDate;
    private Integer timeSlot;
    private Integer quota;
    private Integer soldCount;
    private Integer version;
}
