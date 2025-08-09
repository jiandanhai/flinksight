package com.flinksight.common.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

// 退出响应
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "退出相应DTO")
public class SsoAuthLogoutResponseDTO implements Serializable {
    private String message;
}