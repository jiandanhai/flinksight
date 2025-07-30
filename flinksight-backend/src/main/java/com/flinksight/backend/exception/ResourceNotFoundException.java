package com.flinksight.backend.exception;

import com.flinksight.common.enums.ErrorCode;

public class ResourceNotFoundException extends BusinessException {
    public ResourceNotFoundException(String message) {
        super(ErrorCode.FORBIDDEN, message);
    }
}