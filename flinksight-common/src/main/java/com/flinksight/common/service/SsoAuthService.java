package com.flinksight.common.service;


import com.flinksight.common.dto.*;

/**
 * SSO 认证服务接口
 */
public interface SsoAuthService {
    /**
     * 用户登录（验证账号密码，返回JWT和用户信息）
     */
    SsoAuthResponseDTO login(SsoAuthLoginRequestDTO dto);

    SsoAuthResponseDTO register(SsoAuthRegisterRequestDTO req);

    SsoAuthLogoutResponseDTO logout(Long userId, String token);


     boolean validateToken(String token);


    SsoAuthResponseDTO getUserFromToken(String token);

    /**
     * 用SSO回调code换取用户信息，生成本地token
     */
    SsoAuthResponseDTO handleSsoCallback(String code, String codeVerifier);

}
