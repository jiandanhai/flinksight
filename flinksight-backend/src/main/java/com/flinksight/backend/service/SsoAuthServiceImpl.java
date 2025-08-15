package com.flinksight.backend.service;


import com.flinksight.backend.domain.Role;
import com.flinksight.backend.domain.User;
import com.flinksight.backend.mapper.UserStructMapper;
import com.flinksight.backend.repository.RoleRepository;
import com.flinksight.backend.repository.UserRepository;
import com.flinksight.backend.security.jwt.JwtUtil;
import com.flinksight.common.dto.*;
import com.flinksight.common.service.SsoAuthService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.util.StringUtils;
import org.springframework.web.client.RestTemplate;

import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.Collections;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * SSO 认证服务实现
 */
@Service
@RequiredArgsConstructor
@Transactional
public class SsoAuthServiceImpl implements SsoAuthService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final UserStructMapper userStructMapper;
    private final JwtUtil jwtUtil;
    private final RestTemplate restTemplate;
    private final PasswordEncoder passwordEncoder;

    @Value("${sso.oauth2.token-url}")
    private String tokenUrl;
    @Value("${SSO_OAUTH2_USERINFO_URL}")
    private String userInfoUrl;
    @Value("${sso.oauth2.client-id}")
    private String clientId;
    @Value("${sso.oauth2.client-secret}")
    private String clientSecret;
    @Value("${sso.oauth2.callback-url}")
    private String callbackUrl;

    @Value("${business.default-user-role:USER}")
    private String defaultUserRoleCode;

    @Override
    public SsoAuthResponseDTO login(SsoAuthLoginRequestDTO dto) {
        User user = userRepository.findByUsername(dto.getAccount())
                .orElseThrow(() -> new RuntimeException("用户不存在"));
        if (!passwordEncoder.matches(dto.getPassword(), user.getPassword())) {
            throw new RuntimeException("密码错误");
        }
            UserDTO userDTO = userStructMapper.toDTO(user);

        SsoAuthResponseDTO ssoAuthResponseDTO = new SsoAuthResponseDTO();
        ssoAuthResponseDTO.setToken(jwtUtil.generateToken(user.getId(), user.getUsername(),user.getTenantId()));
        ssoAuthResponseDTO.setUser(userDTO);
        ssoAuthResponseDTO.setRedirectUrl(callbackUrl);
        return ssoAuthResponseDTO;
    }

    @Override
    public SsoAuthResponseDTO register(SsoAuthRegisterRequestDTO req) {
        if (userRepository.existsByUsername(req.getAccount())) {
            throw new RuntimeException("账号已存在");
        }
        User user = User.builder()
                .username(req.getAccount())
                .password(passwordEncoder.encode(req.getPassword()))
                .nickname(req.getNickname())
                .email(req.getEmail())
                .build();
        User saved = userRepository.save(user);
        UserDTO ssoUserDTO = userStructMapper.toDTO(saved);
        SsoAuthResponseDTO ssoAuthResponseDTO = new SsoAuthResponseDTO();
        ssoAuthResponseDTO.setToken(jwtUtil.generateToken(user.getId(), user.getUsername(),user.getTenantId()));
        ssoAuthResponseDTO.setUser(ssoUserDTO);
        return ssoAuthResponseDTO;
    }

    @Override
    public SsoAuthLogoutResponseDTO logout(Long userId, String token) {
        // 可记录logout日志或黑名单token等
        SsoAuthLogoutResponseDTO resp = new SsoAuthLogoutResponseDTO();
        resp.setMessage("登出成功");
        return resp;
    }

    @Override
    public boolean validateToken(String token) {
        return jwtUtil.validateToken(token);
    }

    @Override
    public SsoAuthResponseDTO getUserFromToken(String token) {
        Long userId = jwtUtil.getUserIdFromToken(token);
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("用户不存在"));
        UserDTO userDTO = userStructMapper.toDTO(user);
        SsoAuthResponseDTO authResponseDTO = new SsoAuthResponseDTO();
        authResponseDTO.setToken(token);
        authResponseDTO.setUser(userDTO);
        authResponseDTO.setRedirectUrl(callbackUrl);
        return authResponseDTO;
    }

    /**
     * SSO认证回调处理：用code换access_token和用户信息，查建本地用户，生成token，返回给前端
     */
    @Override
    public SsoAuthResponseDTO handleSsoCallback(String code, String codeVerifier) {
        // —— 1) 用 code 换 access_token / id_token
        MultiValueMap<String, String> form = new LinkedMultiValueMap<>();
        form.add("grant_type", "authorization_code");
        form.add("code", code);
        form.add("client_id", clientId);
        if (StringUtils.hasText(clientSecret)) {
            form.add("client_secret", clientSecret);
        }
        form.add("redirect_uri", callbackUrl);

        // 如果提供了 code_verifier，则加入验证
        if (StringUtils.hasText(codeVerifier)) {
            form.add("code_verifier", codeVerifier);  // PKCE 流程
        }

        HttpHeaders tokenHeaders = new HttpHeaders();
        tokenHeaders.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        Map<String, Object> tokenResp = restTemplate.postForObject(StringUtils.trimWhitespace(tokenUrl), new HttpEntity<>(form, tokenHeaders), Map.class);
        if (tokenResp == null || !tokenResp.containsKey("access_token")) {
            throw new IllegalStateException("SSO token exchange failed: no access_token");
        }
        String accessToken = (String) tokenResp.get("access_token");
        String idToken     = (String) tokenResp.getOrDefault("id_token", null);

        // —— 2) 拉取 /userinfo（如果你有 userInfoUrl），并解析关键字段
        Map<String, Object> userInfoResp = Collections.emptyMap();
        if (StringUtils.hasText(StringUtils.trimWhitespace(userInfoUrl))) {
            HttpHeaders headers = new HttpHeaders();
            headers.setBearerAuth(accessToken);
            userInfoResp = restTemplate.exchange(
                    StringUtils.trimWhitespace(userInfoUrl), HttpMethod.GET, new HttpEntity<>(headers), Map.class
            ).getBody();
            if (userInfoResp == null) userInfoResp = Collections.emptyMap();
        }

        // ---------- 解析 ssoId / username / nickname / avatar ----------
        String ssoId = asStr(userInfoResp.get("sub")); // 首选 userinfo.sub

        // 候选的用户名字段（有些 IdP 不返回 preferred_username）
        String preferredUsername = firstNonBlank(
                asStr(userInfoResp.get("preferred_username")),
                asStr(userInfoResp.get("email")),
                asStr(userInfoResp.get("login")),
                asStr(userInfoResp.get("name"))
        );
        String username = firstNonBlank(preferredUsername, "sso_user");
        String nickname = firstNonBlank(asStr(userInfoResp.get("name")), username);
        String avatar   = firstNonBlank(asStr(userInfoResp.get("avatar")), asStr(userInfoResp.get("picture")));

        // 2nd：从 id_token 解析 sub（推荐；Keycloak 一般都会给 id_token）
        if (!hasText(ssoId) && hasText(idToken)) {
            ssoId = getJwtClaim(idToken, "sub");
            // 如果 userinfo 没有用户名字段，也可以从 id_token 兜底
            if (!hasText(username))  username  = firstNonBlank(getJwtClaim(idToken, "preferred_username"),
                    getJwtClaim(idToken, "email"),
                    "sso_user");
            if (!hasText(nickname))  nickname  = firstNonBlank(getJwtClaim(idToken, "name"), username);
            if (!hasText(avatar))    avatar    = firstNonBlank(getJwtClaim(idToken, "picture"), getJwtClaim(idToken, "avatar"));
        }

        // 3rd：若 access_token 也是 JWT（大多是），再尝试从里面取 sub
        if (!hasText(ssoId) && hasText(accessToken) && isJwtLike(accessToken)) {
            ssoId = getJwtClaim(accessToken, "sub");
        }

        // 最终兜底：仍拿不到稳定 ID，建议直接报错（不要用 username 代替！）
        if (!hasText(ssoId)) {
            throw new IllegalStateException("无法解析稳定的 SSO subject(sub)，拒绝创建/绑定本地用户。");
        }

        final String finalSsoId    = ssoId;
        final String finalUsername = username;
        final String finalNickname = nickname;
        final String finalAvatar   = avatar;

        // 3. 查找或自动创建本地用户
        User ssoUser = userRepository.findBySsoId(ssoId)
                .orElseThrow(() -> new IllegalStateException("用户未注册，请先进行注册"));

        // 4. 生成JWT token
        String token = jwtUtil.generateToken(ssoUser.getId(), ssoUser.getUsername(),ssoUser.getTenantId(),ssoUser.getRoles().stream().map(Role::getCode)
                .collect(Collectors.toSet()));

        // 5. 封装前端需要的DTO
        UserDTO ssoUserDTO = new UserDTO();
        ssoUserDTO.setId(ssoUser.getId());
        ssoUserDTO.setUsername(ssoUser.getUsername());
        ssoUserDTO.setNickname(ssoUser.getNickname());
        ssoUserDTO.setAvatar(ssoUser.getAvatar());
        ssoUserDTO.setRoles(ssoUser.getRoles().stream()
                .map(r -> new RoleDTO(r.getId(), r.getCode(), r.getName()))
                .collect(Collectors.toList()));
        return new SsoAuthResponseDTO(token, ssoUserDTO);
    }


    private static boolean hasText(String s) { return s != null && !s.isBlank(); }
    private static String asStr(Object v) { return v == null ? null : String.valueOf(v); }
    private static String firstNonBlank(String... arr) {
        for (String s : arr) if (hasText(s)) return s;
        return null;
    }
    private static boolean isJwtLike(String token) {
        // 简单判断：三段 & 头部是 Base64Url
        return token != null && token.chars().filter(ch -> ch == '.').count() == 2;
    }

    /** 仅用于提取 claim（不做签名校验）。千万不要据此做鉴权！ */
    private static String getJwtClaim(String jwt, String claim) {
        try {
            String[] parts = jwt.split("\\.");
            if (parts.length != 3) return null;
            byte[] payload = Base64.getUrlDecoder().decode(parts[1]);
            // 用最轻量的方式取字段，避免引入大依赖；你也可替换成 Jackson 解析
            String json = new String(payload, StandardCharsets.UTF_8);
            // 极简解析：建议换成 Jackson ObjectMapper 更稳
            // 这里提供 Jackson 版本（推荐）：
            com.fasterxml.jackson.databind.JsonNode node =
                    new com.fasterxml.jackson.databind.ObjectMapper().readTree(json);
            var n = node.get(claim);
            return n == null ? null : n.isTextual() ? n.asText() : n.asText(null);
        } catch (Exception e) {
            return null;
        }
    }
}
