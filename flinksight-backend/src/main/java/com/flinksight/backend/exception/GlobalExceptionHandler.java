package com.flinksight.backend.exception;

import com.flinksight.backend.common.ApiResponse;
import com.flinksight.backend.security.tenant.TenantException;
import com.flinksight.common.enums.ErrorCode;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

/**
 * 全局异常处理，统一所有异常返回标准API响应
 */
@RestControllerAdvice
public class GlobalExceptionHandler {

    // 业务异常
    @ExceptionHandler(BusinessException.class)
    public ApiResponse<Void> handleBusiness(BusinessException ex) {
        return ApiResponse.error(ex.getCode(), ex.getMessage());
    }

    // 多租户异常
    @ExceptionHandler(TenantException.class)
    public ApiResponse<Void> handleTenant(TenantException ex) {
        return ApiResponse.error(ex.getCode(), ex.getMessage());
    }

    // 参数/非法参数
    @ExceptionHandler(IllegalArgumentException.class)
    public ApiResponse<Void> handleIllegalArg(IllegalArgumentException ex) {
        return ApiResponse.error(ErrorCode.PARAM_INVALID, ex.getMessage());
    }

    // 认证异常
    @ExceptionHandler(AuthException.class)
    public ApiResponse<Void> handleAuth(AuthException ex) {
        return ApiResponse.error(ex.getCode(), ex.getMessage());
    }

    // 权限异常
    @ExceptionHandler(ForbiddenException.class)
    public ApiResponse<Void> handleForbidden(ForbiddenException ex) {
        return ApiResponse.error(ex.getCode(), ex.getMessage());
    }

    // 资源不存在
    @ExceptionHandler(ResourceNotFoundException.class)
    public ApiResponse<Void> handleNotFound(ResourceNotFoundException ex) {
        return ApiResponse.error(ErrorCode.NOT_FOUND, ex.getMessage());
    }

    // 兜底：其它未捕获异常
    @ExceptionHandler(Exception.class)
    public ApiResponse<Void> handleUnknown(Exception ex) {
        // 可打印日志/traceId等
        return ApiResponse.error(ErrorCode.UNKNOWN_ERROR, "系统异常：" + ex.getMessage());
    }
}