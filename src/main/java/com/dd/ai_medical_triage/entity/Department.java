package com.dd.ai_medical_triage.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@TableName("department")
public class Department {
    /*
    *科室信息实体类
    * 对应数据库表：department
    *
    */
    @TableId(value = "department_id",type = IdType.AUTO)
    @NotBlank( message = "科室ID不能为空")
    private Long departmentId;

    /**
     * 科室名称
     * 对应文档4 department表：name（varchar(45)）
     */
    @NotBlank( message = "科室名称不能为空")
    private String name;
    /**
     * 科室描述
     * 对应文档4 department表：description（text）
     *  */
    private String description;
}
