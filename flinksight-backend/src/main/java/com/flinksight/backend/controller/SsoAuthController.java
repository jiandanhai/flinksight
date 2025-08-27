package com.flinksight.backend.controller;

import com.flinksight.backend.common.ApiResponse;
import com.flinksight.common.dto.SsoAuthLoginRequestDTO;
import com.flinksight.common.dto.SsoAuthRegisterRequestDTO;
import com.flinksight.common.dto.SsoAuthResponseDTO;
import com.flinksight.common.service.SsoAuthService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.util.StringUtils;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.SecureRandom;
import java.util.Base64;
import java.util.Optional;
import java.util.UUID;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/sso")
@Tag(name = "api", description = "SSO单点登录API")
@Validated
public class SsoAuthController {
    private final SsoAuthService ssoAuthService;

    @Value("${SSO_OAUTH2_AUTHORIZE_URL}")
    private String ssoAuthorizeUrl;
    @Value("${SSO_OAUTH2_TOKEN_URL}")
    private String tokenUrl; // 如需 server side 换 token
    @Value("${SSO_OAUTH2_CLIENT_ID}")
    private String clientId;
    @Value("${SSO_OAUTH2_CALLBACK_URL}")
    private String callbackUrl;
    @Value("${FRONT_URL}")
    private String frontUrl;

    // 保留你的原字段（不删除）
    @Value("${SSO_OAUTH2_END_SESSION_URL}")
    private String endSessionUrl;

    // ========== 登录入口：增加 state/PKCE（在保留你原逻辑基础上增强） ==========
    @Operation(summary = "SSO 登录入口（重定向到认证中心）")
    @GetMapping("/sso-login")
    public void ssoLogin(HttpServletResponse resp,
                         @RequestParam(required = false) String redirect,
                         HttpServletRequest req) throws IOException {
        String state = (redirect == null || redirect.isBlank()) ? "/dashboard" : redirect;

        // 生成 state Nonce 并写入 HttpOnly Cookie（5分钟有效）
        String stateNonce = UUID.randomUUID() + ":" + state;
        writeCookie(resp, "OIDC_STATE", stateNonce, 300);

        // 生成 PKCE：code_verifier 存 Cookie，授权时带上 S256 challenge
        String codeVerifier = base64Url(SecureRandom.getSeed(32));
        writeCookie(resp, "PKCE_VERIFIER", codeVerifier, 300);
        String codeChallenge = s256Base64Url(codeVerifier);

        String ssoUrl = ssoAuthorizeUrl
                + "?client_id=" + url(clientId)
                + "&redirect_uri=" + url(callbackUrl)
                + "&response_type=code"
                + "&scope=" + url("openid profile email")
                + "&code_challenge=" + url(codeChallenge)
                + "&code_challenge_method=S256"
                + "&state=" + url(stateNonce);

        log.info("#[SSO Login] redirect => {}", ssoUrl);
        resp.sendRedirect(ssoUrl);
    }

    // ========== 回调：增加 state 校验与 PKCE 读取（兼容你现有服务接口） ==========
    @Operation(summary = "SSO 回调（处理授权码 -> 颁发本地JWT）")
    @GetMapping("/callback")
    public void ssoCallback(HttpServletResponse resp,
                            @RequestParam String code,
                            @RequestParam(required = false) String state,
                            HttpServletRequest req) throws IOException {
        log.info("#[SSO Callback] code={}, state={}", code, state);

        try {
            // 1) 校验 state（和 Cookie 中的 OIDC_STATE 比对前缀）
            String stateCookie = readCookie(req, "OIDC_STATE");
            if (!StringUtils.hasText(stateCookie) || !stateCookie.startsWith(Optional.ofNullable(state).orElse(""))) {
                log.warn("[SSO Callback] state mismatch. cookie={}, param={}", stateCookie, state);
                resp.sendError(HttpServletResponse.SC_BAD_REQUEST, "state mismatch");
                return;
            }

            // 2) 读取 PKCE verifier（如果服务层支持，可一并传递）
            String codeVerifier = readCookie(req, "PKCE_VERIFIER");

            // 3) 调用 SSO 服务处理回调，传递 code 和 codeVerifier
            SsoAuthResponseDTO loginResponse = ssoAuthService.handleSsoCallback(code, codeVerifier);
            String token = loginResponse.getToken();

            log.info("#[SSO Callback] token => {}", token);

            Long tenantId = loginResponse.getUser().getTenantId();
            if (tenantId == null) {
                tenantId = 1L;  // Default tenantId
            }
            log.info("#[SSO Callback] tenantId => {}", tenantId);

            String safeRedirect = extractRedirectFromState(stateCookie).orElse("/dashboard");
            log.info("#[SSO Callback] safeRedirect => {}", safeRedirect);

            String targetUrl = frontUrl + "/login/sso-callback"
                    + "?token=" + url(token)
                    + "&redirect=" + url(safeRedirect)
                    + "&tenantId=" + url(String.valueOf(tenantId));

            log.info("#[SSO Callback] redirect(front) => {}", targetUrl);

            resp.sendRedirect(targetUrl);
        } catch (Exception e) {
            log.error("[SSO Callback] Error during SSO callback handling", e);
            throw e;
        }
    }

    // ========== 账号密码登录（原样保留） ==========
    @Operation(summary = "账号密码登录", operationId = "login")
    @PostMapping("/login")
    public ApiResponse<SsoAuthResponseDTO> login(@RequestBody @Valid SsoAuthLoginRequestDTO req) {
        log.info("[account login  account=  {}", req.getAccount());
        return ApiResponse.ok(ssoAuthService.login(req));
    }

    // ========== 用户注册（原样保留） ==========
    @Operation(summary = "注册", operationId = "register")
    @PostMapping("/register")
    public ApiResponse<SsoAuthResponseDTO> register(@RequestBody @Valid SsoAuthRegisterRequestDTO req) {
        return ApiResponse.ok(ssoAuthService.register(req));
    }

    // ========== SSO 登出（兼容 yml 与 env 的 end-session-url；保留你原方法不删） ==========
    @Operation(summary = "SSO 登出（重定向至 IdP 退出）",
            description = "退出 Keycloak 会话，可携带 id_token_hint 与 post_logout_redirect_uri")
    @GetMapping("/logout")
    public void ssoLogout(HttpServletRequest req,
                          HttpServletResponse resp,
                          @RequestParam(required = false) String redirect,
                          @RequestParam(required = false) String idTokenHint // 若前端能带上更好
    ) throws IOException {
        // 允许通过 Cookie 获取 id_token（如你在登录后曾存过）
        if (!StringUtils.hasText(idTokenHint)) {
            idTokenHint = readCookie(req, "ID_TOKEN");
        }

        String postLogoutRedirect = StringUtils.hasText(redirect) ? redirect : frontUrl + "/login";

        if (!StringUtils.hasText(endSessionUrl)) {
            // 未配置 end-session-url，退化为直接回前端
            log.warn("[SSO Logout] end-session-url not set, fallback redirect => {}", postLogoutRedirect);
            resp.sendRedirect(postLogoutRedirect);
            return;
        }

        // 保证第一个参数前带有 ?
        StringBuilder logout = new StringBuilder(endSessionUrl);
        if (endSessionUrl.indexOf('?') < 0) {
            logout.append('?');
        } else if (!endSessionUrl.endsWith("&") && !endSessionUrl.endsWith("?")) {
            logout.append('&');
        }
        logout.append("post_logout_redirect_uri=").append(url(postLogoutRedirect));
        if (StringUtils.hasText(idTokenHint)) {
            logout.append("&id_token_hint=").append(url(idTokenHint));
        }

        String logoutUrl = logout.toString();
        log.info("[SSO Logout] redirect to IdP => {}", logoutUrl);
        resp.sendRedirect(logoutUrl);
    }

    // ======= 你原有的工具方法（保留） =======
    private static String url(String v) {
        return URLEncoder.encode(v, StandardCharsets.UTF_8);
    }
    private static String buildQuery(String key, String value) {
        if (!StringUtils.hasText(value)) return "";
        return (key.contains("?") ? "&" : (value.contains("?") ? "&" : (value.startsWith("&") ? "" : (value.startsWith("?") ? "" : "")))) +
                (key + "=" + url(value));
    }
    private static String readCookie(HttpServletRequest req, String name) {
        Cookie[] cs = req.getCookies();
        if (cs == null) return null;
        for (Cookie c : cs) {
            if (name.equals(c.getName())) return c.getValue();
        }
        return null;
    }

    // ======= 新增的内部工具：更安全的 Cookie/PKCE/state 处理（不新建文件） =======
    private static void writeCookie(HttpServletResponse resp, String name, String value, int maxAgeSec) {
        Cookie c = new Cookie(name, value);
        c.setHttpOnly(true);
        c.setPath("/");
        c.setMaxAge(maxAgeSec);
        // 可按需：c.setSecure(true); // 若全站 HTTPS
        resp.addCookie(c);
    }
    private static String base64Url(byte[] bytes) {
        return Base64.getUrlEncoder().withoutPadding().encodeToString(bytes);
    }
    private static String s256Base64Url(String input) {
        try {
            byte[] d = MessageDigest.getInstance("SHA-256").digest(input.getBytes(StandardCharsets.US_ASCII));
            return base64Url(d);
        } catch (Exception e) {
            throw new IllegalStateException(e);
        }
    }
    private static Optional<String> extractRedirectFromState(String stateNonce) {
        if (!StringUtils.hasText(stateNonce)) return Optional.empty();
        int i = stateNonce.indexOf(':');
        if (i < 0 || i + 1 >= stateNonce.length()) return Optional.empty();
        return Optional.of(stateNonce.substring(i + 1));
    }
    private static String firstNonBlank(String... vals) {
        if (vals == null) return null;
        for (String v : vals) {
            if (StringUtils.hasText(v)) return v;
        }
        return null;
    }
}