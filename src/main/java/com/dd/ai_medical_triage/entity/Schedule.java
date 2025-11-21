package com.dd.ai_medical_triage.entity;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Schedule {
    private int id;
    private int doctorId;
    private Date workDate;
    private int timeSlot;
    private int quota;
    private int soldCount;
    private int version;
}
