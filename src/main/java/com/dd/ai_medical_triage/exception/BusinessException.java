package com.dd.ai_medical_triage.exception;

import com.dd.ai_medical_triage.enums.ErrorCode.ErrorCode;
import lombok.Getter;

/**
 * 业务异常
 */
@Getter
public class BusinessException extends RuntimeException {
    private final String code;
    private final Integer standardCode;

    public BusinessException(ErrorCode errorCode) {
        super(errorCode.getMessage());
        this.code = errorCode.getCode();
        this.standardCode = errorCode.getStandardCode();
    }

    public BusinessException(ErrorCode errorCode, String message) {
        super(message);
        this.code = errorCode.getCode();
        this.standardCode = errorCode.getStandardCode();
    }
}