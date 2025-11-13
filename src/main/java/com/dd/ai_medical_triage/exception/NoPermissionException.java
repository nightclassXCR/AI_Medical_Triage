package com.dd.ai_medical_triage.exception;

import com.dd.ai_medical_triage.enums.ErrorCode.ErrorCode;

/**
 * 权限不足异常（管理员接口校验失败时抛出）
 */
public class NoPermissionException extends BusinessException {
  public NoPermissionException(String message) {
    super(ErrorCode.PERMISSION_DENIED, message);
  }

  public NoPermissionException() {
    super(ErrorCode.PERMISSION_DENIED);
  }
}