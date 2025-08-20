package com.flinksight.backend.security.jwt;

import com.flinksight.backend.domain.User;
import com.flinksight.backend.mapper.UserStructMapper;
import com.flinksight.backend.security.SecurityUser;
import com.flinksight.backend.security.tenant.TenantContext;
import com.flinksight.common.dto.UserDTO;
import com.flinksight.common.service.UserService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.util.matcher.RequestMatcher;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * JWT认证过滤器，支持RBAC权限装载
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class JwtAuthFilter extends OncePerRequestFilter {
    private final JwtUtil jwtProvider;
    private final UserService userService;
    private final UserStructMapper userStructMapper;

    // ==== 新增：可选的路径匹配器，用于只在命中时才执行 ====
    private RequestMatcher requestMatcher;

    /**
     * 由SecurityConfig注入：限定此过滤器只在某些路径上生效（如 /api/**）
     */
    public void setRequestMatcher(RequestMatcher requestMatcher) {
        this.requestMatcher = requestMatcher;
    }

    /**
     * 未命中匹配器则不执行过滤逻辑（留给后续链条）
     */
    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) {
        // 未配置匹配器：默认不过滤，避免误拦所有请求
        return requestMatcher == null || !requestMatcher.matches(request);
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain)
            throws ServletException, IOException {

        // 1. 解析Token
        String token = resolveToken(request);
        // 先拿租户：JWT → Header → Query
        String tenantStr = null;
        if (StringUtils.hasText(token) && jwtProvider.validateToken(token)) {
            tenantStr = String.valueOf(jwtProvider.getTenantIdFromToken(token)); // null 安全，返回 null 或 "123"
        }
        if (!StringUtils.hasText(tenantStr)) tenantStr = request.getHeader("X-Tenant-Id");
        if (!StringUtils.hasText(tenantStr)) tenantStr = request.getParameter("tenantId");

        Long tenantId = null;
        if (StringUtils.hasText(tenantStr)) {
            try { tenantId = Long.parseLong(tenantStr.trim()); } catch (NumberFormatException ignore) {}
        }
        if (tenantId != null) {
            TenantContext.setTenantId(tenantId); // 👈 必须：在任何 Service 前
        }
        log.info("[JWT Filter] tenantId from token: {}",tenantStr);
        try {
            if (StringUtils.hasText(token) && jwtProvider.validateToken(token)) {
                String username = jwtProvider.getUsernameFromToken(token);
                Long userId = jwtProvider.getUserIdFromToken(token);

                // 到这里 TenantContext 已经就绪，不会再被 TenantAspect 拦住
                Optional<UserDTO> userOpt = userService.getUserById(userId);
                if (userOpt.isPresent()) {
                    User user = userStructMapper.toEntity(userOpt.get());
                    List<String> perms = userService.getAuthorities(user.getId(),user.getTenantId());
                    List<GrantedAuthority> authorities = perms.stream()
                            .filter(Objects::nonNull)
                            .map(String::trim)
                            .filter(s -> !s.isEmpty())
                            .map(String::toUpperCase)   // 统一
                            .distinct()
                            .map(SimpleGrantedAuthority::new)
                            .collect(Collectors.toList());
                    // 构造SecurityUser
                    SecurityUser securityUser = new SecurityUser(user, new ArrayList<>(perms));
                    UsernamePasswordAuthenticationToken authentication =
                            new UsernamePasswordAuthenticationToken(securityUser, null, authorities);
                    SecurityContextHolder.getContext().setAuthentication(authentication);
                }
            }

            filterChain.doFilter(request, response);
        } finally {
            TenantContext.clear(); // 👈 必须，线程复用会串租户
        }
    }

    /**
     * 解析请求头/参数中的token（常见是 Authorization: Bearer ...）
     */
    private String resolveToken(HttpServletRequest request) {
        String bearerToken = request.getHeader("Authorization");
        if (StringUtils.hasText(bearerToken) && bearerToken.startsWith("Bearer ")) {
            return bearerToken.substring(7);
        }
        // 支持URL参数token等扩展
        return request.getParameter("token");
    }
}
