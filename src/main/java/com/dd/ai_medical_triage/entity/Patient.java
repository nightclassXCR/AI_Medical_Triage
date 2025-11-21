package com.dd.ai_medical_triage.entity;


import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.dd.ai_medical_triage.enums.SimpleEnum.GenderEnum;
import jakarta.validation.constraints.NotBlank;
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
@TableName("patient")
public class Patient {

    /**
     * 患者ID
     * 对应文档4 patient表：patient_id（bigint AI PK）
     */
    @TableId(type = IdType.AUTO)
    @NotBlank(message = "患者ID不能为空")
    private Long patientId;

    /**
     * 用户ID
     * 对应文档4 patient表：user_id（bigint）
     */
    private Long userId;

    /**
     * 患者姓名
     * 对应文档4 patient表：name（varchar(50)）
     */
    private String name;

    /**
     * 年龄
     * 对应文档4 patient表：age
     */
    private String age;

    /**
     * 性别
     * 对应文档4 patient表：gender
     */
    private GenderEnum gender;

    /**
     * 手机号码
     * 对应文档4 patient表：phone_number
     */
    private String phoneNumber;

    /**
     * 身份证号码
     * 对应文档4 patient表：id_card
     */
    private String idCard;

    /**
     * 身高
     * 对应文档4 patient表：height
     */
    private String height;

    /**
     * 体重
     * 对应文档4 patient表：weight
     */
    private String weight;

    /**
     * 病例编号
     * 对应文档4 patient表：case_number
     */
    private String caseNumber;

    /**
     * 创建时间
     * 对应文档4 patient表：create_time
     */
    private LocalDateTime createTime;

}
