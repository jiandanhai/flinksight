package com.flinksight.backend.service;

import com.flinksight.backend.security.tenant.TenantContext;
import com.flinksight.common.dto.AuditLogDTO;
import com.flinksight.common.service.AuditLogService;
import com.flinksight.common.service.OpAudit;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.core.annotation.Order;
import org.springframework.expression.ExpressionParser;
import org.springframework.expression.spel.standard.SpelExpressionParser;
import org.springframework.expression.spel.support.StandardEvaluationContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

/**
 * 操作审计AOP切面，实现自动日志记录
 */
@Aspect
@Component
@Slf4j
@RequiredArgsConstructor
@Order(99) // 权限校验、租户校验之后
public class AuditLogAspect {

    private final AuditLogService auditLogService;
    private  final HttpServletRequest httpServletRequest;

    @Around("@annotation(auditAnno)")
    public Object doAudit(ProceedingJoinPoint joinPoint, OpAudit auditAnno) throws Throwable {
        Object result = null;
        boolean success = false;
        Exception exception = null;
        try {
            result = joinPoint.proceed();
            success = true;
            return result;
        } catch (Exception ex) {
            exception = ex;
            throw ex;
        } finally {
            // 日志采集
            try {
                MethodSignature signature = (MethodSignature) joinPoint.getSignature();
                String action = auditAnno.action();
                String targetType = auditAnno.targetType();
                // 解析目标ID和内容支持SpEL
                String targetId = parseSpEL(auditAnno.targetIdSpEL(), signature, joinPoint.getArgs(), result);
                String content = parseSpEL(auditAnno.contentSpEL(), signature, joinPoint.getArgs(), result);

                // 从SecurityContext、TenantContext获取上下文
                Long userId = getCurrentUserId();
                Long tenantId = TenantContext.getTenantId();

                // 采集请求信息
                String ip = httpServletRequest.getRemoteAddr();

                AuditLogDTO auditLogDTO = AuditLogDTO.builder()
                        .tenantId(tenantId)
                        .userId(userId)
                        .action(action)
                        .targetType(targetType)
                        .targetId(targetId == null ? null : String.valueOf(targetId))
                        .ip(ip)
                        .content(content)
                        .isDeleted(0)
                        .createTime(LocalDateTime.now())
                        .build();
                auditLogService.createAuditLog(auditLogDTO);
            } catch (Exception logEx) {
                log.warn("自动审计日志采集失败", logEx);
            }
        }
    }

    private String parseSpEL(String spel, MethodSignature signature, Object[] args, Object returnValue) {
        if (spel == null || spel.isEmpty()) return null;
        try {
            ExpressionParser parser = new SpelExpressionParser();
            StandardEvaluationContext ctx = new StandardEvaluationContext();
            // 填充入参
            String[] paramNames = signature.getParameterNames();
            for (int i = 0; i < paramNames.length; i++) {
                ctx.setVariable(paramNames[i], args[i]);
            }
            ctx.setVariable("ret", returnValue);
            return parser.parseExpression(spel).getValue(ctx, String.class);
        } catch (Exception ex) {
            return null;
        }
    }

    private Long getCurrentUserId() {
        // 你的SecurityContext里UserId的实现（可根据实际项目定制）
        try {
            Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            // 例如 principal为UserDetailImpl，则可 return ((UserDetailImpl)principal).getId();
            // 此处仅演示
            return principal instanceof Number ? ((Number) principal).longValue() : null;
        } catch (Exception ex) {
            return null;
        }
    }
}
