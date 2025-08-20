package com.flinksight.backend.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.util.matcher.RequestMatcher;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Optional;

/**
 * 鉴权过滤器（一次性拦截、校验权限）
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class PermissionCheckFilter extends OncePerRequestFilter {
    private final PermissionFilterProperties props;
    private final ApiPermissionResolver resolver; // 你已有的实现
    @Qualifier("publicMatchers")
    private final List<RequestMatcher> skipMatchers;

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) {
        if (!props.isEnabled()) return true;

        final String uri = request.getRequestURI();
        final String apiPrefix = props.getApiPrefix();

        // 仅拦截 /api/**（或你配置的前缀），其余一律放过，避免静态等被误伤
        if (apiPrefix != null && !apiPrefix.isBlank() && !uri.startsWith(apiPrefix)) {
            return true;
        }

        // 命中统一白名单（含 application.yml + menu-map 合并结果） -> 跳过
        for (RequestMatcher m : skipMatchers) {
            if (m.matches(request)) {
                return true;
            }
        }
        return false;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest req, HttpServletResponse res, FilterChain chain)
            throws ServletException, IOException {

        Optional<String> need = resolver.resolve(req);
        if (need.isEmpty()) {
            chain.doFilter(req, res);
            return;
        }

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        boolean ok = auth != null && auth.isAuthenticated() &&
                auth.getAuthorities().stream()
                        .map(GrantedAuthority::getAuthority)
                        .anyMatch(a -> a.equals(need.get()));

        if (!ok) {
            res.setStatus(HttpServletResponse.SC_FORBIDDEN);
            res.setCharacterEncoding(StandardCharsets.UTF_8.name());
            res.setContentType("text/plain;charset=UTF-8");
            res.getWriter().write("Forbidden: need authority " + need.get());
            log.debug("[perm] deny {} {} -> need={}", req.getMethod(), req.getRequestURI(), need.get());
            return;
        }

        // 鉴权通过
        chain.doFilter(req, res);
    }
}
