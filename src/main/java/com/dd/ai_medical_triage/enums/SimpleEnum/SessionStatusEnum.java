package com.dd.ai_medical_triage.enums.SimpleEnum;

import com.baomidou.mybatisplus.annotation.EnumValue;

/**
 * AI 会话状态枚举类
 */
public enum SessionStatusEnum {

    /** 会话已开始 */
    STARTED("started"),

    /** 会话进行中 */
    ACTIVE("active"),

    /** 会话已关闭 */
    CLOSED("closed"),

    /** 会话已超时 */
    TIMEOUT("timeout"),

    /** 会话异常 */
    ERROR("error");

    @EnumValue // 告诉 MyBatis-Plus：数据库存的是这个 value
    private final String value;

    SessionStatusEnum(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}