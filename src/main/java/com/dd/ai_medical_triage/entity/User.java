package com.dd.ai_medical_triage.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import com.dd.ai_medical_triage.enums.SimpleEnum.GenderEnum;
import com.dd.ai_medical_triage.enums.SimpleEnum.UserRoleEnum;
import com.dd.ai_medical_triage.enums.SimpleEnum.UserStatusEnum;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;

import java.time.LocalDateTime;

/**
 * 用户实体类
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@TableName("`user`")
public class User{

    /**
     * 无参构造方法（如public User(){}）的作用是创建一个 "空对象"（所有字段为默认值）
     * 校验注解（@NotBlank等）本身不会阻止创建对象，只会在触发校验时（如 Controller 层接收请求参数时）生效
     * 直接通过new User()创建对象时，即使字段为空也不会触发校验，只有在校验器执行时才会检查
     * 当使用校验框架（如 Spring Validation）对通过无参构造创建的对象进行校验时，会触发注解规则，导致校验失败
     */

    // ===================== 1. 基础主键与账号信息（严格匹配数据库表） =====================
    /**
     * 用户唯一标识（数据库主键，自动递增）
     * 对应文档4 user表：user_id（bigint AI PK）
     */
    @TableId(value = "user_id", type = IdType.AUTO)
    private Long userId;

    /**
     * 登录密码（BCrypt加密存储）
     * 对应文档4 user表：password（varchar(45)）；文档3要求加密
     */
    @NotBlank(message = "密码不能为空")
    @Length(min = 6, max = 20, message = "密码长度需6-20位")
    private String password;

    /**
     * 用户名（显示用，非登录账号）
     * 对应文档4 user表：user_name（varchar(45)）
     */
    @NotBlank(message = "用户名不能为空")
    @Length(min = 2, max = 15, message = "用户名长度需2-15位")
    private String username;

    /**
     * 邮箱（登录/找回密码用）
     * 对应文档4 user表：email（varchar(45)）；文档1注册需求
     */
    @Email(message = "邮箱格式不正确")
    private String email;

    /**
     * 手机号（登录/验证码用）
     * 对应文档4 user表：phone_number（bigint）；文档1手机号注册需求
     */
    private String phoneNumber;

    // ===================== 2. 个人资料信息（文档1个人中心需求） =====================
    /**
     * 头像路径（阿里云OSS存储URL）
     * 对应文档4 user表：avatar_url（varchar(512)）；文档2云服务集成
     */
    private String avatarUrl;

    /**
     * 个人简介
     * 对应文档5原字段：bio（补充长度限制，避免过长）
     */
    @Length(max = 200, message = "个人简介不超过200字")
    private String bio;

    /**
     * 性别（可扩展为枚举，此处保留字符串兼容灵活性）
     * 对应文档5原字段：gender
     */
    private GenderEnum gender;

    // ===================== 3. 状态与时间信息（文档2系统设计要求） =====================
    /**
     * 注册时间
     * 对应文档5原字段：createTime；记录用户创建时间
     */
    private LocalDateTime createTime;

    /**
     * 最后活跃时间（登录/发帖/交易时更新）
     * 对应文档5原字段：activityTime；用于活跃度统计
     */
    private LocalDateTime activityTime;

    /**
     * 账号状态（激活/禁用/封禁）
     * 对应文档5原字段：status（枚举类型）；文档1账号安全需求
     */
    private UserStatusEnum status;

    /**
     * 用户角色（普通用户/管理员/吧主）
     * 对应文档5原字段：role（枚举类型）；文档2权限管理需求
     */
    private UserRoleEnum role;


    // ===================== 4. 业务辅助方法（简化Service层逻辑） =====================
    /**
     * 判断是否为管理员（文档2权限校验需求）
     */
    public boolean isAdmin() {
        return UserRoleEnum.ADMIN.equals(this.role);
    }

    /**
     * 判断账号是否可用（文档1账号安全需求）
     */
    public boolean isActive() {
        return UserStatusEnum.NORMAL.equals(this.status);
    }

}
