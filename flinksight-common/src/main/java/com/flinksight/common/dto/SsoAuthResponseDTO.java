package com.flinksight.common.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * SSO 单点登录响应对象
 * 返回token及用户基本信息
 */
@Data
@Builder
@NoArgsConstructor
@Schema(description = "单点登录相应DTO")
public class SsoAuthResponseDTO implements Serializable {
    @Schema(description = "JWT认证成功后返回的JWT令牌")
    private String token;       // 登录token
    private UserDTO user;   // 当前登录用户基本信息
    private String redirectUrl;   // （可选）登录后跳转目标地址
    public SsoAuthResponseDTO(String token, UserDTO user) {
        this.token = token;
        this.user = user;
    }

    // ★ 必须加上这个三参构造方法 ↓↓↓
    public SsoAuthResponseDTO(String token, UserDTO user, String redirectUrl) {
        this.token = token;
        this.user = user;
        this.redirectUrl = redirectUrl;
    }
}
