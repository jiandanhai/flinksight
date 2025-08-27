package com.flinksight.backend.exception;

public class BusinessException extends RuntimeException {
    private final int code;
    public BusinessException(int code, String msg) {
        super(msg);
        this.code = code;
    }
    public int getCode() { return code; }

    public static BusinessException notFound(String msg){ return new BusinessException(404, msg); }
    public static BusinessException conflict(String msg){ return new BusinessException(409, msg); }
    public static BusinessException forbidden(String msg){ return new BusinessException(403, msg); }
    public static BusinessException badRequest(String msg){ return new BusinessException(400, msg); }
}