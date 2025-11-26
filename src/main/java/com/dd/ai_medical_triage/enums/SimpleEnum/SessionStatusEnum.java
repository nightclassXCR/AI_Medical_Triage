package com.dd.ai_medical_triage.enums.SimpleEnum;

/**
 * AI 会话状态枚举类
 */
public enum SessionStatusEnum {

    /** 会话已开始 */
    STARTED,

    /** 会话已结束 */
    FINISHED,

    /** 会话已取消 */
    CANCELLED,

    /** 会话已关闭 */
    CLOSED,

    /** 会话已超时 */
    TIMEOUT,

    /** 会话异常 */
    ERROR
}
