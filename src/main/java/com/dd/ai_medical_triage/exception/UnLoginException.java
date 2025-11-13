package com.dd.ai_medical_triage.exception;

import com.dd.ai_medical_triage.enums.ErrorCode.ErrorCode;

public class UnLoginException extends BusinessException {
    public UnLoginException(String message) {
        super(ErrorCode.PERMISSION_UNAUTHORIZED, message);
    }

    public UnLoginException() {
        super(ErrorCode.PERMISSION_UNAUTHORIZED);
    }
}
