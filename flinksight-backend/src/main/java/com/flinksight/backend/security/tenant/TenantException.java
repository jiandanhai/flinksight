package com.flinksight.backend.security.tenant;

import com.flinksight.backend.exception.BusinessException;
import com.flinksight.backend.exception.GlobalExceptionHandler;
import com.flinksight.common.enums.ErrorCode;

/**
 * 租户串租、未授权时抛出的业务异常
 */
public class TenantException extends BusinessException {
    public TenantException(String message) {
        super(ErrorCode.TENANT_ISOLATION, message);
    }
}