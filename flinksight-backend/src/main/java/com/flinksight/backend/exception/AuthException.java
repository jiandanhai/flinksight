package com.flinksight.backend.exception;

import com.flinksight.common.enums.ErrorCode;

/**
 * 认证相关异常
 */
public class AuthException extends BusinessException {
    public AuthException(String message) {
        super(ErrorCode.UNAUTHORIZED, message);
    }
}
