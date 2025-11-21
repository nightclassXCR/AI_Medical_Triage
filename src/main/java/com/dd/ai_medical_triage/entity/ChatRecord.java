package com.dd.ai_medical_triage.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
/**
 * 会话实体类
 * 对应数据库表：chat_record
 * 聊天记录
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@TableName("chat_record")
public class ChatRecord {
    /**
     * 会话ID
     * 对应文档4 chat_record表：chat_id（int AI PK）
     */
    @TableId(value = "chat_id",type = IdType.AUTO)
    @NotBlank(message = "会话ID不能为空")
    private Long chatId;

    /**
     * 患者ID
     * 对应文档4 chat_record表：patient_id（int）
     */
    @TableField(value = "patient_id")
    @NotBlank(message = "患者ID不能为空")
    private Long patientId;

    /**
     * 时间戳
     * 对应文档4 chat_record表：timestamp（datetime）
     */
    private LocalDateTime timestamp;

    /**
     * 患者问题
     * 对应文档4 chat_record表：question（text）
     */
    private String question;

    /**
     * AI回复
     * 对应文档4 chat_record表：ai_reply（text）
     */
    private String aiReply;
}
