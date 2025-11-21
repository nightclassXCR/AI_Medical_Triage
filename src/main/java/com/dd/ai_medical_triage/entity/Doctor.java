package com.dd.ai_medical_triage.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
/**
 * 医生实体类
 * 对应数据库表：doctor
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@TableName("doctor")
public class Doctor {
    /**
     * 用户ID
     * 对应文档4 user表：user_id（bigint）
     * 外键
     */
    @TableField(value = "user_id")
    @NotBlank(message = "用户ID不能为空")
    private Long userId;

    /**
     * 医生ID
     * 对应文档4 doctor表：doctor_id（int AI PK）
     */
    @TableId(value = "doctor_id",type = IdType.AUTO)
    @NotBlank(message = "医生ID不能为空")
    private Long doctorId;

    /**
     * 科室ID
     * 对应文档4 doctor表：department_id（int）
     */
    @NotBlank(message = "科室ID不能为空")
    private Long departmentId;
    /**
     * 医生姓名
     * 对应文档4 doctor表：name（varchar(50)）
     */
    @NotBlank(message = "医生姓名不能为空")
    private String name;
    /**
     * 医生职称
     * 对应文档4 doctor表：title（varchar(30)）
     */
    @NotBlank(message = "医生职称不能为空")
    private String title;          // 主任、主治等

    private String phone;
    /**
     * 医生状态
     * 对应文档4 doctor表：status（int）
     * 1代表出诊，0代表闲置
     */
    private Integer status;

}
