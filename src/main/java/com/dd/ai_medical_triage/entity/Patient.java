package com.dd.ai_medical_triage.entity;


import com.dd.ai_medical_triage.enums.SimpleEnum.GenderEnum;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * 患者信息实体类
 * 对应数据库表：patient
 * 患者信息
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Patient {

    private int id;

    private String name;

    private String age;

    private GenderEnum gender;

    private String phoneNumber;

    private String idCard;

    private String height;

    private String weight;

    private String caseNumber;

    private LocalDateTime createTime;

}
