package com.dd.ai_medical_triage.Entity;


import com.dd.ai_medical_triage.enums.SimpleEnum.GenderEnum;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

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
