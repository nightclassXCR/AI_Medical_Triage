package com.dd.ai_medical_triage.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.dd.ai_medical_triage.enums.SimpleEnum.SessionStatusEnum;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
/**
 * 会话实体类
 * 对应数据库表：session
 */

@Data
@AllArgsConstructor
@NoArgsConstructor
@TableName("session")
public class Session {
    /**
     * 会话ID
     * 对应文档4 session表：session_id（bigint AI PK）
     */
    @TableId(value = "session_id",type = IdType.AUTO)
    @NotBlank(message = "会话ID不能为空")
    private Long sessionId;

    /**
     * 患者ID
     * 对应文档4 session表：patient_id（bigint）
     */
    private Long patientId;
    /**
     * 创建时间
     * 对应文档4 session表：created_time（datetime）
     */
    private LocalDateTime createdTime;
    /**
     * 会话状态
     * 对应文档4 session表：status（tinyint）
     */
    private SessionStatusEnum status;
}
