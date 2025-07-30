package com.flinksight.backend.common;

import com.flinksight.common.enums.ErrorCode;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * 统一API响应结构
 */
@Data
@Schema(description = "统一返回对象")
public class ApiResponse<T> {
    @Schema(description = "是否成功")
    private boolean success;
    private int code;
    @Schema(description = "错误信息")
    private String message;
    @Schema(description = "返回数据")
    private T data;
    @Schema(description = "租户ID")
    private String traceId;

    public static <T> ApiResponse<T> ok(T data) {
        ApiResponse<T> r = new ApiResponse<>();
        r.success = true;
        r.code = ErrorCode.SUCCESS;
        r.message = "success";
        r.data = data;
        r.traceId = TraceUtil.getCurrentTraceId(); // 如有可加
        return r;
    }
    public static <T> ApiResponse<T> error(int code,String message) {
        ApiResponse<T> r = new ApiResponse<>();
        r.success = false;
        r.code = code;
        r.message = message;
        r.data = null;
        r.traceId = TraceUtil.getCurrentTraceId();
        return r;
    }
}
