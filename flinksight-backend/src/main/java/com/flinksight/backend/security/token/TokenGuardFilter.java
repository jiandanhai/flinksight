package com.flinksight.backend.security.token;

import com.flinksight.backend.security.jwt.JwtUtil;
import com.flinksight.common.service.TokenVersionService;
import com.flinksight.common.service.UserService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.List;

/** 快速拒绝已撤销/版本不匹配的 token；放在 JwtAuthFilter 之前 */
@Component
@RequiredArgsConstructor
public class TokenGuardFilter extends OncePerRequestFilter {

    private final JwtUtil jwtUtil;
    private final List<TokenVersionService> tokenServices;   // 同时注入两种实现，任一拒绝就拦截
    @Nullable private final UserService userService;  // 仅版本号策略需要

    @Override
    protected void doFilterInternal(HttpServletRequest req, HttpServletResponse resp, FilterChain chain)
            throws ServletException, IOException {

        String bearer = req.getHeader("Authorization");
        if (!StringUtils.hasText(bearer) || !bearer.startsWith("Bearer ")) {
            chain.doFilter(req, resp); return;
        }
        String token = bearer.substring(7);

        // 1) 黑名单：任一实现报告撤销，直接 401
        for (TokenVersionService ts : tokenServices) {
            try { if (ts.isRevoked(token)) { unauthorized(resp, "TOKEN_REVOKED"); return; } }
            catch (Throwable ignore) {}
        }

        // 2) 版本号：如 JWT 含版本号，则与当前版本比对
        try {
            Long uid = jwtUtil.getUserIdFromToken(token);
            if (uid != null && userService != null) {
                Integer vClaim = jwtUtil.getTokenVersionFromToken(token);
                if (vClaim != null) {
                    int current = userService.getTokenVersion(uid);
                    if (vClaim != current) { unauthorized(resp, "TOKEN_VERSION_MISMATCH"); return; }
                }
            }
        } catch (Throwable ignore) {}

        chain.doFilter(req, resp);
    }

    private void unauthorized(HttpServletResponse resp, String code) throws IOException {
        resp.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        resp.setContentType("application/json;charset=UTF-8");
        resp.getWriter().write("{\"error\":\"" + code + "\"}");
    }
}
