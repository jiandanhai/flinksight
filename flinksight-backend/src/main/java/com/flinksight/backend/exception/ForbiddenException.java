package com.flinksight.backend.exception;

import com.flinksight.common.enums.ErrorCode;

/**
 * 权限相关异常
 */
public class ForbiddenException extends BusinessException {
    public ForbiddenException(String message) {
        super(ErrorCode.FORBIDDEN, message);
    }
}