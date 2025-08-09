package com.flinksight.backend.controller;

import com.flinksight.backend.common.ApiResponse;
import com.flinksight.common.dto.*;
import com.flinksight.common.service.SsoAuthService;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/sso")
public class SsoAuthController {
    private final SsoAuthService ssoAuthService;
     @Value("${sso.oauth2.authorize-url}")
     private String ssoAuthorizeUrl;
     @Value("${sso.oauth2.token-url}")
     private String tokenUrl; // 如需 server side 换 token
     @Value("${sso.oauth2.client-id}")
     private String clientId;
     @Value("${sso.oauth2.callback-url}")
     private String callbackUrl;
     @Value("${front.url}")
     private String frontUrl;

    /**
     * SSO 登录入口 - 重定向到认证中心
     */
    @GetMapping("/sso-login")
    public void ssoLogin(HttpServletResponse resp,
                         @RequestParam(required = false) String redirect) throws IOException {
        String state = (redirect == null || redirect.isBlank()) ? "/dashboard" : redirect;
        String ssoUrl = ssoAuthorizeUrl
                + "?client_id=" + URLEncoder.encode(clientId, StandardCharsets.UTF_8)
                + "&redirect_uri=" + URLEncoder.encode(callbackUrl, StandardCharsets.UTF_8)  // ← 这里
                + "&response_type=code"
                + "&state=" + URLEncoder.encode(state, StandardCharsets.UTF_8);
        System.out.println("[SSO Login] Redirecting to SSO server: {"+ssoUrl+"}");
        resp.sendRedirect(ssoUrl);
    }

    /**
     * SSO 回调 - 处理授权码并生成 Token
     */
    @GetMapping("/callback")
    public void ssoCallback(HttpServletResponse resp,
                            @RequestParam String code,
                            @RequestParam(required = false) String state) throws IOException {
        // 1. 用授权码换取登录信息（token）
        SsoAuthResponseDTO loginResponse = ssoAuthService.handleSsoCallback(code);
        String token = loginResponse.getToken();

        // 2. 动态获取租户 ID
        // 假设从登录后的用户信息获取 tenantId
        Long tenantId = loginResponse.getUser().getTenantId();
        if (tenantId == null) {
            tenantId = 1L;  // 兜底处理，如果没有租户 ID，给一个默认值
        }

        // 3. 确定回跳地址
        // - 如果 state 为空，默认回 dashboard（防止循环回登录页）
        // - 对 token 和 redirect 统一进行 URL 编码，避免特殊字符出错
        String safeRedirect = (state == null || state.isBlank()) ? "/dashboard" : state;
        String targetUrl = frontUrl + "/login/sso-callback"
                + "?token=" + URLEncoder.encode(token, StandardCharsets.UTF_8)
                + "&redirect=" + URLEncoder.encode(safeRedirect, StandardCharsets.UTF_8)
                + "&tenantId=" + URLEncoder.encode(String.valueOf(tenantId), StandardCharsets.UTF_8);  // 关键：附加 tenantId

        // 4. 打印调试日志（生产可保留 info 级别，便于排查）
        System.out.println("SSO Callback] code = {" + code + "}");
        System.out.println("[SSO Callback] token = {" + token + "}");
        System.out.println("[SSO Callback] tenantId = {" + tenantId + "}");  // 调试用
        System.out.println("[SSO Callback] redirect(front-end) =  {" + targetUrl + "}");

        // 5. 重定向到前端
        resp.sendRedirect(targetUrl);
    }

    /**
     * 账号密码登录
     */
    @Operation(summary = "账号密码登录", operationId = "login")
    @PostMapping("/login")
    public ApiResponse<SsoAuthResponseDTO> login(@RequestBody @Valid SsoAuthLoginRequestDTO req) {
        return ApiResponse.ok(ssoAuthService.login(req));
    }

    /**
     * 用户注册
     */
    @Operation(summary = "注册", operationId = "register")
    @PostMapping("/register")
    public ApiResponse<SsoAuthResponseDTO> register(@RequestBody @Valid SsoAuthRegisterRequestDTO req) {
        return ApiResponse.ok(ssoAuthService.register(req));
    }

    /**
     * 退出登录
     */
    @Operation(summary = "退出", operationId = "logout")
    @PostMapping("/logout")
    public ApiResponse<SsoAuthLogoutResponseDTO> logout(@RequestHeader("Authorization") String token,
                                                        @RequestParam Long userId) {
        return ApiResponse.ok(ssoAuthService.logout(userId, token));
    }
}
