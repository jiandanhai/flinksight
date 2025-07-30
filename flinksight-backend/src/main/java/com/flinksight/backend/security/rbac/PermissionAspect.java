package com.flinksight.backend.security.rbac;

import com.flinksight.backend.exception.ForbiddenException;
import com.flinksight.common.service.PermissionService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.*;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;

/**
 * RBAC权限注解AOP切面
 * 拦截所有加了@OpPermission注解的Controller/Service，进行权限校验
 */
@Aspect
@Component
@Slf4j
@RequiredArgsConstructor
public class PermissionAspect {
    private final PermissionService permissionService;

    @Before("@annotation(com.flinksight.backend.security.rbac.OpPermission) || @within(com.flinksight.backend.security.rbac.OpPermission)")
    public void checkPermission(JoinPoint joinPoint) {
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        Method method = signature.getMethod();

        OpPermission permission = method.getAnnotation(OpPermission.class);
        // 优先取方法上的@Permission，否则取类上的
        if (permission == null) {
            permission = joinPoint.getTarget().getClass().getAnnotation(OpPermission.class);
        }
        if (permission == null) {
            return; // 没有注解则跳过
        }

        String permissionCode = permission.value();
        Long userId = getCurrentUserId();

        // ===== 你的权限校验实现，建议调用RBACService/PermissionService校验 =====
        boolean hasPermission = permissionService.userHasPermission(userId, permissionCode);
        if (!hasPermission) {
            log.warn("用户{}无权限操作：{}", userId, permissionCode);
            throw new ForbiddenException("没有操作权限：" + permissionCode);
        }
    }

    /**
     * 获取当前登录用户ID（需结合你自己的UserDetails实现补充）
     */
    private Long getCurrentUserId() {
        try {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            Object principal = authentication.getPrincipal();
            // 如principal为UserDetails，可强转并getId()
            // return ((YourUserDetailsImpl) principal).getId();
            // 如principal直接为userId，则：
            if (principal instanceof Number) {
                return ((Number) principal).longValue();
            }
            // TODO: 如principal为username，可通过UserService查ID
            return null;
        } catch (Exception ex) {
            return null;
        }
    }

}
