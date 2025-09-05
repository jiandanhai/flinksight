package com.flinksight.backend.service;

import com.flinksight.backend.domain.Role;
import com.flinksight.backend.domain.User;
import com.flinksight.backend.mapper.UserStructMapper;
import com.flinksight.backend.repository.RoleRepository;
import com.flinksight.backend.repository.UserRepository;
import com.flinksight.backend.security.jwt.JwtUtil;
import com.flinksight.common.dto.RoleDTO;
import com.flinksight.common.dto.SsoAuthLogoutResponseDTO;
import com.flinksight.common.dto.SsoAuthResponseDTO;
import com.flinksight.common.dto.UserDTO;
import com.flinksight.common.service.SsoAuthService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.util.StringUtils;
import org.springframework.web.client.RestTemplate;

import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.*;
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

    /** 是否允许首次登录自动创建本地影子账号（JIT） */
    @Value("${auth.jit-create:false}")
    private boolean jitCreateEnabled;

    /** 可选：显式配置 OIDC Issuer；若未配置，将从 tokenUrl 推导 JWK 地址 */
    @Value("${SSO_OIDC_ISSUER:}")
    private String issuer;

    @Override
    public SsoAuthLogoutResponseDTO logout(Long userId, String token) {
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
        if (StringUtils.hasText(codeVerifier)) {
            form.add("code_verifier", codeVerifier);  // PKCE
        }

        HttpHeaders tokenHeaders = new HttpHeaders();
        tokenHeaders.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        @SuppressWarnings("unchecked")
        Map<String, Object> tokenResp = restTemplate.postForObject(
                StringUtils.trimWhitespace(tokenUrl),
                new HttpEntity<>(form, tokenHeaders),
                Map.class
        );
        if (tokenResp == null || !tokenResp.containsKey("access_token")) {
            throw new IllegalStateException("SSO token exchange failed: no access_token");
        }
        String accessToken = (String) tokenResp.get("access_token");
        String idToken     = (String) tokenResp.getOrDefault("id_token", null);

        // —— 2) 验签 + 解析 claims
        JwtDecoder decoder = buildJwtDecoderFromConfig();
        Jwt idJwt = null;
        Jwt atJwt = null;
        if (StringUtils.hasText(idToken)) {
            idJwt = decoder.decode(idToken);
        }
        if (StringUtils.hasText(accessToken)) {
            atJwt = decoder.decode(accessToken);
        }
        if (idJwt == null && atJwt == null) {
            throw new IllegalStateException("SSO token exchange failed: neither id_token nor access_token available");
        }

        // —— 3) 组装关键信息（issuer/subject/ssoId/username/昵称/邮箱等）
        String iss  = idJwt != null ? idJwt.getIssuer().toString()
                : (atJwt != null ? atJwt.getIssuer().toString() : null);
        String sub  = idJwt != null ? idJwt.getSubject()
                : (atJwt != null ? atJwt.getSubject() : null);

        Map<String, Object> userInfoResp = Collections.emptyMap();
        if (StringUtils.hasText(userInfoUrl)) {
            try {
                HttpHeaders headers = new HttpHeaders();
                headers.setBearerAuth(accessToken);
                ResponseEntity<Map> resp = restTemplate.exchange(
                        StringUtils.trimWhitespace(userInfoUrl),
                        HttpMethod.GET,
                        new HttpEntity<>(headers),
                        Map.class
                );
                if (resp.getStatusCode().is2xxSuccessful() && resp.getBody() != null) {
                    //noinspection unchecked
                    userInfoResp = resp.getBody();
                }
            } catch (Exception ignore) { /* userinfo 失败不阻断登录 */ }
        }

        // 规范化 sso_id：优先用 iss#sub；iss 缺失时退化为 sub；都没有再做非校验兜底
        String ssoId;
        if (hasText(iss) && hasText(sub)) {
            ssoId = iss + "#" + sub;
        } else if (hasText(asStr(userInfoResp.get("iss"))) && hasText(asStr(userInfoResp.get("sub")))) {
            ssoId = asStr(userInfoResp.get("iss")) + "#" + asStr(userInfoResp.get("sub"));
        } else if (hasText(asStr(userInfoResp.get("sub")))) {
            ssoId = asStr(userInfoResp.get("sub"));
        } else {
            ssoId = null;
        }
        if (!hasText(ssoId)) {
            if (hasText(idToken)) ssoId = nonSecureJoinIssSub(idToken);
            if (!hasText(ssoId) && hasText(accessToken)) ssoId = nonSecureJoinIssSub(accessToken);
        }
        if (!hasText(ssoId)) {
            throw new IllegalStateException("无法解析稳定的 SSO 标识（iss/sub），拒绝创建/绑定本地用户。");
        }

        String preferredUsername = firstNonBlank(
                idJwt != null ? idJwt.getClaimAsString("preferred_username") : null,
                asStr(userInfoResp.get("preferred_username")),
                asStr(userInfoResp.get("email")),
                asStr(userInfoResp.get("login")),
                asStr(userInfoResp.get("name"))
        );
        String username = firstNonBlank(preferredUsername, sub, "sso_user");
        String nickname = firstNonBlank(
                idJwt != null ? idJwt.getClaimAsString("name") : null,
                asStr(userInfoResp.get("name")),
                username
        );
        String avatar = firstNonBlank(
                idJwt != null ? idJwt.getClaimAsString("picture") : null,
                asStr(userInfoResp.get("avatar")),
                asStr(userInfoResp.get("picture"))
        );
        String email = firstNonBlank(
                idJwt != null ? idJwt.getClaimAsString("email") : null,
                asStr(userInfoResp.get("email"))
        );
        Boolean emailVerified = idJwt != null
                ? Optional.ofNullable(asBool(idJwt.getClaim("email_verified"))).orElse(false)
                : Optional.ofNullable(asBool(userInfoResp.get("email_verified"))).orElse(false);

        // —— 4) 查找或创建本地用户（遵守 jitCreateEnabled 开关）
        // 先按规范的 ssoId 查；查不到，再按“旧格式 sub”查，命中后自动升级
        User ssoUser = userRepository.findBySsoId(ssoId).orElse(null);
        if (ssoUser == null) {
            String legacySub = (idJwt != null ? idJwt.getSubject()
                    : (atJwt != null ? atJwt.getSubject() : asStr(userInfoResp.get("sub"))));
            if (hasText(legacySub) && !legacySub.equals(ssoId)) {
                ssoUser = userRepository.findBySsoId(legacySub).orElse(null);
            }
        }

        if (ssoUser == null) {
            if (!jitCreateEnabled) {
                throw new IllegalStateException("用户未注册，请先在系统开通账号");
            }
            // 首登自动建“影子用户”
            ssoUser = new User();
            ssoUser.setSsoId(ssoId);           // 直接落规范化 sso_id
            ssoUser.setIdpIssuer(iss);         // 回填 issuer
            ssoUser.setIdpSubject(sub);        // 回填 subject
            ssoUser.setUsername(username);
            ssoUser.setDisplayName(nickname);
            ssoUser.setEmail(email);
            ssoUser.setEmailVerified(Boolean.TRUE.equals(emailVerified));
            ssoUser.setStatus(1);
            // 如有默认租户策略可设置：ssoUser.setTenantId(1L);
            ssoUser = userRepository.save(ssoUser);

            // 可选：赋默认角色（保持你原逻辑）
            if (StringUtils.hasText(defaultUserRoleCode)) {
                Long tenantId = Optional.ofNullable(ssoUser.getTenantId()).orElse(1L);
                Optional<Role> roleOpt = roleRepository.findByTenantIdAndCode(tenantId, defaultUserRoleCode)
                        .or(() -> roleRepository.findByTenantIdIsNullAndCode(defaultUserRoleCode));
                if (roleOpt.isPresent()) {
                    Role role = roleOpt.get();
                    if (ssoUser.getRoles() == null) ssoUser.setRoles(new java.util.HashSet<>());
                    if (!ssoUser.getRoles().contains(role)) {
                        ssoUser.getRoles().add(role);
                        userRepository.save(ssoUser);
                    }
                }
            }
        } else {
            // 命中了旧数据：自动升级为规范 sso_id，并回填 issuer/subject
            boolean upgraded = false;
            if (hasText(ssoId) && !Objects.equals(ssoUser.getSsoId(), ssoId)) {
                ssoUser.setSsoId(ssoId); upgraded = true;
            }
            if (hasText(iss) && !Objects.equals(ssoUser.getIdpIssuer(), iss)) {
                ssoUser.setIdpIssuer(iss); upgraded = true;
            }
            if (hasText(sub) && !Objects.equals(ssoUser.getIdpSubject(), sub)) {
                ssoUser.setIdpSubject(sub); upgraded = true;
            }
            if (upgraded) {
                ssoUser = userRepository.save(ssoUser);
            }
        }

        // —— 5) 每次登录同步资料 + 最近登录时间（不动授权）
        boolean changed = false;
        if (hasText(username) && !username.equals(ssoUser.getUsername())) {
            ssoUser.setUsername(username); changed = true;
        }
        if (hasText(nickname) && !nickname.equals(ssoUser.getDisplayName())) {
            ssoUser.setDisplayName(nickname); changed = true;
        }
        if (hasText(avatar) && !Objects.equals(avatar, ssoUser.getAvatar())) {
            ssoUser.setAvatar(avatar); changed = true;
        }
        if (hasText(email) && !Objects.equals(email, ssoUser.getEmail())) {
            ssoUser.setEmail(email); changed = true;
        }
        if (emailVerified != null && !Objects.equals(emailVerified, ssoUser.getEmailVerified())) {
            ssoUser.setEmailVerified(emailVerified); changed = true;
        }
        ssoUser.setLastLoginAt(LocalDateTime.now()); changed = true;
        if (changed) {
            ssoUser = userRepository.save(ssoUser);
        }

        // —— 6) 生成业务 JWT
        Set<String> roleCodes = ssoUser.getRoles() == null ? Set.of()
                : ssoUser.getRoles().stream().map(Role::getCode).collect(Collectors.toSet());
        String token = jwtUtil.generateToken(
                ssoUser.getId(), ssoUser.getUsername(), ssoUser.getTenantId(), roleCodes
        );

        // —— 7) 组装返回 DTO
        UserDTO ssoUserDTO = new UserDTO();
        ssoUserDTO.setId(ssoUser.getId());
        ssoUserDTO.setUsername(ssoUser.getUsername());
        ssoUserDTO.setAvatar(ssoUser.getAvatar());
        ssoUserDTO.setRoles(
                (ssoUser.getRoles() == null ? Collections.<RoleDTO>emptyList()
                        : ssoUser.getRoles().stream()
                        .map(r -> new RoleDTO(r.getId(), r.getCode(), r.getName()))
                        .collect(Collectors.toList()))
        );

        SsoAuthResponseDTO resp = new SsoAuthResponseDTO();
        resp.setToken(token);
        resp.setUser(ssoUserDTO);
        // resp.setRedirectUrl(callbackUrl); // 如需回跳地址可启用
        return resp;
    }

    // ====================== 工具方法 ======================

    private static boolean hasText(String s) { return s != null && !s.isBlank(); }

    private static String asStr(Object v) { return v == null ? null : String.valueOf(v); }

    private static String firstNonBlank(String... arr) {
        for (String s : arr) if (hasText(s)) return s;
        return null;
    }

    private static Boolean asBool(Object v) {
        if (v instanceof Boolean b) return b;
        if (v instanceof String s) return "true".equalsIgnoreCase(s) || "1".equals(s);
        if (v instanceof Number n) return n.intValue() != 0;
        return null;
    }

    /** 仅用于兜底拼接 iss#sub（不做签名校验） */
    private static String nonSecureJoinIssSub(String jwt) {
        try {
            String iss = getJwtClaim(jwt, "iss");
            String sub = getJwtClaim(jwt, "sub");
            if (hasText(iss) && hasText(sub)) return iss + "#" + sub;
            return sub;
        } catch (Exception e) {
            return null;
        }
    }

    /** 仅用于提取 claim（不做签名校验）。千万不要据此做鉴权！ */
    private static String getJwtClaim(String jwt, String claim) {
        try {
            String[] parts = jwt.split("\\.");
            if (parts.length != 3) return null;
            byte[] payload = Base64.getUrlDecoder().decode(parts[1]);
            String json = new String(payload, StandardCharsets.UTF_8);
            com.fasterxml.jackson.databind.JsonNode node =
                    new com.fasterxml.jackson.databind.ObjectMapper().readTree(json);
            var n = node.get(claim);
            return n == null ? null : n.isTextual() ? n.asText() : n.asText(null);
        } catch (Exception e) {
            return null;
        }
    }

    /** 基于 issuer 或 tokenUrl 推导 JWK 地址，构建 JwtDecoder */
    private JwtDecoder buildJwtDecoderFromConfig() {
        String jwkSetUri;
        if (StringUtils.hasText(issuer)) {
            String base = issuer.endsWith("/") ? issuer.substring(0, issuer.length() - 1) : issuer;
            jwkSetUri = base + "/protocol/openid-connect/certs";
        } else {
            String u = StringUtils.trimWhitespace(tokenUrl);
            int i = u.indexOf("/protocol/openid-connect/token");
            if (i > 0) {
                jwkSetUri = u.substring(0, i) + "/protocol/openid-connect/certs";
            } else {
                if (u.endsWith("/token")) jwkSetUri = u.substring(0, u.length() - 5) + "certs";
                else throw new IllegalStateException("无法从 tokenUrl 推导 JWK 地址，请配置 SSO_OIDC_ISSUER");
            }
        }
        return NimbusJwtDecoder.withJwkSetUri(jwkSetUri).build();
    }
}
