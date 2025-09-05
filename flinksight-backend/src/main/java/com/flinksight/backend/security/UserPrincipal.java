package com.flinksight.backend.security;

import java.io.Serializable;
import java.util.Set;

/** 放进 SecurityContext 的唯一认证主体 */
public record UserPrincipal(
        Long userId,
        String ssoId,          // 一律 iss#sub
        Long tenantId,         // 当前选中租户（首次可为null或默认租户）
        String username,       // preferred_username
        Set<String> permissions
) implements Serializable {}