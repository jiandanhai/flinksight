package com.flinksight.common.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * SSO 单点登录请求对象
 * 用于接收前端用户名密码等认证信息
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "单点登录请求DTO")
public class SsoAuthLoginRequestDTO implements Serializable {
    @Schema(description = "登录账号", required = true)
    private String account;

    @Schema(description = "登录密码（明文传输，传输层加密）",  required = true)
    private String password;
}
