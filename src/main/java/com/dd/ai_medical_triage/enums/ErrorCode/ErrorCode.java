package com.dd.ai_medical_triage.enums.ErrorCode;

import lombok.Getter;

@Getter
public enum ErrorCode {
    // 通用错误
    SUCCESS("SYSTEM_000", 200, "操作成功"),
    FAILURE("SYSTEM_001", 500, "操作失败"),
    PARAM_ERROR("SYSTEM_002", 400, "参数错误"),
    PARAM_NULL("SYSTEM_003", 400, "参数为空"),
    NOT_FOUND("SYSTEM_004", 404, "资源不存在"),
    RELATED_DATA_MISSING("SYSTEM_005", 400, "缺少关联数据"),

    DATA_UPDATE_FAILED("SYSTEM_011", 500, "数据更新失败"),
    DATA_DELETE_FAILED("SYSTEM_012", 500, "数据删除失败"),
    DATA_INSERT_FAILED("SYSTEM_013", 500, "数据插入失败"),
    DATA_QUERY_FAILED("SYSTEM_014", 500, "数据查询失败"),

    PERMISSION_UNAUTHORIZED("SYSTEM_021", 401, "未登录"),
    PERMISSION_DENIED("SYSTEM_022", 403, "无权限"),

    OPERATION_REPEAT("SYSTEM_051", 409, "操作重复"),

    SYSTEM_ERROR("SYSTEM_100", 500, "系统错误"),

    // 业务错误 - 用户模块
    USER_EXISTS("USER_001", 409, "用户已存在"),
    EMAIL_EXISTS("USER_002", 409, "邮箱已被注册"),
    PHONE_EXISTS("USER_003", 409, "手机号已被注册"),
    USERNAME_EXISTS("USER_004", 409, "用户名已存在"),

    PASSWORD_CONFIRM_NOT_MATCH("USER_005", 400, "密码确认不一致"),
    OLD_PASSWORD_ERROR("USER_006", 400, "旧密码错误"),

    EMAIL_NULL("USER_011", 400, "邮箱为空"),
    PHONE_NULL("USER_012", 400, "手机号为空"),
    USERNAME_NULL("USER_013", 400, "用户名为空"),
    ROLE_NULL("USER_014", 400, "用户角色为空"),
    USER_ID_NULL("USER_015", 400, "用户ID为空"),
    PASSWORD_NULL("USER_016", 400, "密码为空"),

    ROLE_INVALID("USER_021", 400, "用户角色参数错误"),
    STATUS_INVALID("USER_022", 400, "用户状态参数错误"),
    USERNAME_FORMAT_INVALID("USER_023", 400, "用户名格式错误"),
    PHONE_FORMAT_INVALID("USER_024", 400, "手机号格式错误"),
    EMAIL_FORMAT_INVALID("USER_025", 400, "邮箱格式错误"),
    PASSWORD_FORMAT_INVALID("USER_027", 400, "密码格式错误"),
    USERNAME_LENGTH_INVALID("USER_028", 400, "用户名长度错误"),
    AVATAR_URL_FORMAT_INVALID("USER_029", 400, "头像URL格式错误"),

    USER_NOT_EXISTS("USER_051", 404, "用户不存在"),
    PASSWORD_ERROR("USER_052", 401, "密码错误"),

    // 用户第三方绑定相关
    THIRD_AUTH_FAILED("USER_071", 401, "第三方授权失败"),
    THIRD_SYSTEM_ERROR("USER_072", 500, "第三方系统错误"),
    USER_THIRD_PARTY_UNBIND_FAILED("USER_073", 500, "第三方账号解绑失败"),
    USER_THIRD_PARTY_NOT_EXISTS("USER_074", 404, "第三方账号绑定记录不存在"),
    USER_THIRD_PARTY_NOT_BOUND("USER_075", 400, "第三方账号未绑定"),
    USER_THIRD_PARTY_BIND_FAILED("USER_076", 500, "第三方账号绑定失败"),

    // 消息模块
    MESSAGE_NOT_EXISTS("MSG_001", 404, "消息不存在"),
    MESSAGE_ID_NULL("MSG_002", 400, "消息ID为空"),
    MESSAGE_CONTENT_NULL("MSG_003", 400, "消息内容为空"),
    MESSAGE_RECEIVER_NULL("MSG_004", 400, "消息接收者为空"),
    MESSAGE_SENDER_NULL("MSG_005", 400, "消息发送者为空"),
    MESSAGE_TYPE_NULL("MSG_006", 400, "消息类型为空"),

    MESSAGE_TYPE_INVALID("MSG_005", 400, "消息类型参数错误"),
    MESSAGE_STATUS_INVALID("MSG_006", 400, "消息状态参数错误"),
    MESSAGE_CONTENT_INVALID("MSG_007", 400, "消息内容参数错误"),

    RECEIVER_NOT_EXISTS("MSG_011", 404, "接收者不存在"),
    SENDER_NOT_EXISTS("MSG_012", 404, "发送者不存在"),


    MESSAGE_STATUS_TRANSITION_INVALID("MSG_004", 400, "消息状态转换错误"),

    // QA模块
    QA_ID_NULL("QA_001", 400, "QA记录ID为空"),
    QA_QUESTION_TEXT_NULL("QA_002", 400, "QA问题内容为空"),
    QA_NOT_EXISTS("QA_003", 404, "QA记录不存在"),

    // Log模块
    LOG_NOT_EXISTS("LOG_001", 404, "日志记录不存在"),
    LOG_TARGET_ID_NULL("LOG_002", 400, "日志目标ID为空"),
    LOG_TARGET_TYPE_NULL("LOG_003", 400, "日志目标类型为空"),
    LOG_ACTION_TYPE_NULL("LOG_004", 400, "日志操作类型为空"),
    LOG_TARGET_TYPE_INVALID("LOG_005", 400, "日志目标类型参数错误"),
    LOG_ACTION_TYPE_INVALID("LOG_006", 400, "日志操作类型参数错误"),

    // 邮件模块
    MAIL_SEND_FAILS("MAIL_001", 500, "邮件发送失败"),
    MAIL_REDIS_BREAK("MAIL_002", 500, "Redis异常"),
    MAIL_SEND_FAILS_TOO_MANY_TIMES("MAIL_003", 500, "邮件发送失败次数过多"),
    MAIL_VERIFICATION_CODE_EXPIRED("MAIL_004", 400, "邮件验证码已过期"),
    MAIL_VERIFICATION_CODE_INVALID("MAIL_005", 400, "邮件验证码无效"),
    MAIL_VERIFICATION_CODE_NOT_EXISTS("MAIL_006", 404, "邮件验证码不存在"),
    MAIL_VERIFICATION_CODE_NOT_MATCH("MAIL_007", 400, "邮件验证码不匹配"),

    // 验证码模块
    VERIFY_CODE_INVALID("VERIFY_001", 400, "验证码服务出错"),
    VERIFY_CODE_SEND_FAILED("VERIFY_002", 500, "验证码发送失败"),
    VERIFY_CODE_VERIFY_FAILED("VERIFY_003", 400, "验证码验证失败"),
    VERIFY_CODE_NOT_EXISTS("VERIFY_004", 404, "验证码不存在"),
    VERIFY_CODE_EXPIRED("VERIFY_005", 400, "验证码已过期"),
    VERIFY_CODE_NOT_MATCH("VERIFY_006", 400, "验证码不匹配"),

    EMAIL_NOT_BELONG_TO_USER("VERIFY_051", 400, "邮箱不属于当前用户"),
    PHONE_NOT_BELONG_TO_USER("VERIFY_052", 400, "手机号不属于当前用户");

    // HTTP状态码
    // 200：成功操作
    // 400：客户端参数错误 / 请求非法
    // 401：未认证 / 认证失败
    // 403：权限不足
    // 404：资源不存在
    // 409：资源冲突（如重复创建）
    // 500：服务器内部错误

    private final String code;          // 业务错误码
    private final int standardCode;     // 标准错误码（参考HTTP状态码）
    private final String message;       // 错误信息

    ErrorCode(String code, int standardCode, String message) {
        this.code = code;
        this.standardCode = standardCode;
        this.message = message;
    }
}