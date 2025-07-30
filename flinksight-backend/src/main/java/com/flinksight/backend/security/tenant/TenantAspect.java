package com.flinksight.backend.security.tenant;

import com.flinksight.backend.exception.BusinessException;
import com.flinksight.backend.security.tenant.TenantInterceptor;
import com.flinksight.common.enums.ErrorCode;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.*;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;

/**
 * 全局租户串租防护AOP
 * 拦截所有@TenantRequired的方法/类，对所有名为tenantId的入参自动与上下文ThreadLocal做比对
 * 不一致即抛TenantException，防止串租
 */
@Slf4j
@Aspect
@Order(1)
@Component
public class TenantAspect {

    @Around("@within(com.flinksight.backend.security.tenant.TenantRequired) || @annotation(com.flinksight.backend.security.tenant.TenantRequired)")
    public Object validateTenantIsolation(ProceedingJoinPoint joinPoint) throws Throwable {
        Long contextTenantId = TenantInterceptor.getCurrentTenantId();
        if (contextTenantId == null) {
            throw new BusinessException(ErrorCode.TENANT_ISOLATION, "请求未包含合法租户ID，拒绝访问");
        }

        // 校验入参中的tenantId
        Object[] args = joinPoint.getArgs();
        MethodSignature methodSignature = (MethodSignature) joinPoint.getSignature();
        Method method = methodSignature.getMethod();
        String[] paramNames = methodSignature.getParameterNames();

        for (int i = 0; i < args.length; i++) {
            if (paramNames[i].equalsIgnoreCase("tenantId") && args[i] != null) {
                Long paramTenantId = Long.valueOf(args[i].toString());
                if (!contextTenantId.equals(paramTenantId)) {
                    log.warn("租户串租检测: 上下文tenantId={}, 业务入参tenantId={}", contextTenantId, paramTenantId);
                    throw new BusinessException(ErrorCode.TENANT_ISOLATION, "租户隔离校验失败，禁止跨租户操作！");
                }
            }
        }
        return joinPoint.proceed();
    }
}