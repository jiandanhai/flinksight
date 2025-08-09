package com.flinksight.backend.service;


import com.flinksight.backend.domain.Role;
import com.flinksight.backend.domain.User;
import com.flinksight.backend.mapper.UserStructMapper;
import com.flinksight.backend.repository.RoleRepository;
import com.flinksight.backend.repository.UserRepository;
import com.flinksight.backend.security.jwt.JwtUtil;
import com.flinksight.common.dto.*;
import com.flinksight.common.enums.UserStatusEnum;
import com.flinksight.common.service.SsoAuthService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
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
    @Value("${sso.oauth2.userinfo-url}")
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
    public SsoAuthResponseDTO handleSsoCallback(String code) {
        // 1. 用 code 换 SSO access_token
        Map<String, String> tokenReq = new HashMap<>();
        tokenReq.put("grant_type", "authorization_code");
        tokenReq.put("code", code);
        tokenReq.put("client_id", clientId);
        tokenReq.put("client_secret", clientSecret);
        tokenReq.put("redirect_uri", callbackUrl);
        Map tokenResp = restTemplate.postForObject(tokenUrl, tokenReq, Map.class);
        String accessToken = (String) tokenResp.get("access_token");

        // 2. 用 access_token 拉取 SSO 用户信息
        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", "Bearer " + accessToken);
        HttpEntity<?> httpEntity = new HttpEntity<>(headers);
        Map userInfoResp = restTemplate.exchange(userInfoUrl, HttpMethod.GET, httpEntity, Map.class).getBody();

        String ssoId = (String) userInfoResp.get("sub"); // 唯一ID，或openid
        String username = (String) userInfoResp.get("preferred_username");
        String nickname = (String) userInfoResp.get("name");
        String avatar = (String) userInfoResp.get("avatar");

        // 3. 查找或自动创建本地用户
        User ssoUser = userRepository.findBySsoId(ssoId)
                .orElseGet(() -> {
                    // 查找“普通用户”默认角色（建议角色表预置）
                    Role defaultRole = roleRepository.findByCode(defaultUserRoleCode)
                            .orElseThrow(() -> new IllegalStateException("系统未配置默认角色USER"));
                    User newUser = User.builder()
                            .ssoId(ssoId)
                            .username(username)
                            .nickname(nickname)
                            .avatar(avatar)
                            .status(UserStatusEnum.ENABLED.getCode())
                            .build();
                    newUser.getRoles().add(defaultRole); // 自动分配角色
                    return userRepository.save(newUser);
                });

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
}
