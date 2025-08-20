package com.flinksight.backend.security;

import com.flinksight.backend.menu.MenuMap;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.security.web.util.matcher.RequestMatcher;

import java.util.ArrayList;
import java.util.List;

/**
 * 负责“白名单”匹配器的唯一构建处：
 * - 合并 application.yml 的 skip
 * - （可选）合并 menu-map.yaml 的一级/二级菜单 path（自动加 apiPrefix）
 */
@Slf4j
@Configuration
@RequiredArgsConstructor
public class PublicMatchersConfig {

    private final PermissionFilterProperties props;
    private final MenuMap menuMap; // 你已有的 MenuOpenApiConfig 会提供这个 bean

    @Bean
    @Qualifier("publicMatchers")
    public List<RequestMatcher> publicMatchers() {
        List<RequestMatcher> matchers = new ArrayList<>();

        // 1) application.yml 的基础白名单
        for (String s : props.getSkip()) {
            addOneMatcher(matchers, s);
        }

        // 2) menu-map.yaml 一级/二级菜单也并入白名单（可关）
        if (props.isIncludeMenuMap() && menuMap != null && menuMap.getTops() != null) {
            String prefix = normalizePrefix(props.getApiPrefix()); // e.g. "/api"
            menuMap.getTops().forEach(top -> {
                // 顶级
                addOneMatcher(matchers, prefix + ensureLeadingSlash(top.getPath()));
                // 二级
                if (top.getChildren() != null) {
                    top.getChildren().forEach(ch ->
                            addOneMatcher(matchers, prefix + ensureLeadingSlash(ch.getPath()))
                    );
                }
            });
        }

        log.info("[security] publicMatchers size={}", matchers.size());
        return matchers;
    }

    /** 支持 "GET /api/user/me" 或 "/public/**" 两种格式 */
    private void addOneMatcher(List<RequestMatcher> list, String expr) {
        if (expr == null || expr.isBlank()) return;
        String v = expr.trim();
        int sp = v.indexOf(' ');
        if (sp > 0) {
            String method = v.substring(0, sp).trim().toUpperCase();
            String path   = v.substring(sp + 1).trim();
            list.add(new AntPathRequestMatcher(path, method));
        } else {
            list.add(new AntPathRequestMatcher(v));
        }
    }

    private static String ensureLeadingSlash(String p) {
        if (p == null || p.isBlank()) return "/";
        return p.startsWith("/") ? p : ("/" + p);
    }

    private static String normalizePrefix(String p) {
        if (p == null || p.isBlank()) return "";
        return p.startsWith("/") ? p : ("/" + p);
    }
}