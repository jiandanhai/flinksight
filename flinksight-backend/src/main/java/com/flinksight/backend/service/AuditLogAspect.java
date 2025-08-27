package com.flinksight.backend.service;

import com.flinksight.backend.audit.AuditLogEvent;
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
import org.slf4j.MDC;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.core.annotation.Order;
import org.springframework.expression.ExpressionParser;
import org.springframework.expression.spel.standard.SpelExpressionParser;
import org.springframework.expression.spel.support.StandardEvaluationContext;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;

import java.time.LocalDateTime;
import java.util.Optional;

/**
 * 操作审计 AOP 切面（事件优先，服务降级）
 * - 使用方式：在需要审计的方法上标注 @OpAudit(action="...", targetType="...", targetIdSpEL="#id", contentSpEL="'name='+#dto.name")
 * - 记录点：tenantId / userId / operator / ip / traceId / source / result / failReason / content
 * - 事件优先：发布 AuditLogEvent，由 @TransactionalEventListener 在事务提交后写库；
 *   若没有监听器或发布失败，则降级为 auditLogService.createAuditLog(dto) 直写（不影响主流程）
 */
@Aspect
@Component
@Slf4j
@RequiredArgsConstructor
@Order(99)
public class AuditLogAspect {

    private static final int MAX_CONTENT_LEN = 8000;
    private static final int MAX_FAIL_MSG    = 512;

    private final AuditLogService auditLogService;
    private final ApplicationEventPublisher publisher;
    /** 懒取 request，避免异步/非 Web 线程注入失败 */
    private final ObjectProvider<HttpServletRequest> requestProvider;

    private final ExpressionParser parser = new SpelExpressionParser();

    @Around("@annotation(auditAnno)")
    public Object doAudit(ProceedingJoinPoint jp, OpAudit auditAnno) throws Throwable {
        // swagger/actuator 之类不审计
        if (isSkippablePath()) {
            return jp.proceed();
        }

        Object ret = null;
        Exception ex = null;
        boolean success = false;

        try {
            ret = jp.proceed();
            success = true;
            return ret;
        } catch (Exception e) {
            ex = e;
            throw e;
        } finally {
            try {
                MethodSignature sig = (MethodSignature) jp.getSignature();

                // 1) 注解+SPEL
                String action     = nvl(auditAnno.action());
                String targetType = nvl(auditAnno.targetType());
                String targetId   = evalSpELAsString(auditAnno.targetIdSpEL(), sig, jp.getArgs(), ret);
                String detailRaw  = evalSpELAsString(auditAnno.contentSpEL(), sig, jp.getArgs(), ret);

                // 2) 上下文
                Long   tenantId     = currentTenantId();
                Long   userId       = currentUserId();
                String operator     = currentUsername();
                String operatorId   = currentOperatorId(userId);
                String traceId      = currentTraceId();
                String ip           = clientIp();
                String source       = requestSource();

                // 3) 结果
                String result     = success ? "SUCCESS" : "FAIL";
                String failReason = success ? null : truncate(ex.getClass().getSimpleName() + ":" + ex.getMessage(), MAX_FAIL_MSG);

                // 4) 统一内容
                String content = buildContentJson(detailRaw, result, failReason);

                LocalDateTime now = LocalDateTime.now();
                AuditLogDTO dto = AuditLogDTO.builder()
                        .tenantId(tenantId)
                        .userId(userId)
                        .operatorId(operatorId)
                        .operatorName(operator)
                        .operateTime(now)
                        .createTime(now)
                        .action(action)
                        .targetType(targetType)
                        .targetId(blank(targetId) ? null : targetId.trim())
                        .content(truncate(content, MAX_CONTENT_LEN))
                        .source(source)
                        .traceId(nvl(traceId))
                        .result(result)
                        .failReason(failReason)
                        .ip(ip)
                        .isDeleted(0)
                        .build();

                // 5) 事件优先；失败则降级直写
                if (tryPublishEvent(dto)) {
                    log.debug("[Audit] event published. action={}, targetType={}, targetId={}", action, targetType, targetId);
                } else {
                    auditLogService.createAuditLog(dto);
                    log.debug("[Audit] fallback saved by service. action={}, targetType={}, targetId={}", action, targetType, targetId);
                }
            } catch (Exception logEx) {
                // 审计失败绝不影响主流程
                log.warn("[Audit] write failed: {}", logEx.getMessage(), logEx);
            }
        }
    }

    /* ======================= 事件优先，服务降级 ======================= */

    /** 发布领域事件（事务存在时由监听器在提交后落库）。失败返回 false 走服务降级 */
    private boolean tryPublishEvent(AuditLogDTO dto) {
        try {
            // 如果你项目已有 AuditLogEvent 类，优先使用事件；没有则直接返回 false 让服务降级
            AuditLogEvent evt = AuditLogEvent.from(dto);
            // 若当前线程未绑定事务，照样发布（监听器可不要求事务）
            publisher.publishEvent(evt);
            return true;
        } catch (Throwable t) {
            return false;
        }
    }

    /* ======================= SpEL & 工具 ======================= */

    private String evalSpELAsString(String spel, MethodSignature sig, Object[] args, Object ret) {
        if (blank(spel)) return null;
        try {
            StandardEvaluationContext ctx = new StandardEvaluationContext();
            String[] names = sig.getParameterNames();
            if (names != null) {
                for (int i = 0; i < names.length; i++) {
                    ctx.setVariable(names[i], args[i]);
                }
            }
            ctx.setVariable("ret", ret);
            return parser.parseExpression(spel).getValue(ctx, String.class);
        } catch (Exception e) {
            log.debug("[Audit] SpEL parse failed ({}) : {}", spel, e.getMessage());
            return null;
        }
    }

    /* ======================= 上下文采集 ======================= */

    private Long currentTenantId() {
        try {
            Long t = TenantContext.getTenantId();
            if (t != null) return t;
        } catch (Exception ignore) {}
        Long fromMdc = parseLong(MDC.get("tenantId"));
        if (fromMdc != null) return fromMdc;
        return parseLong(getRequestSafe().map(r -> r.getHeader("X-Tenant-Id")).orElse(null));
    }

    private Long currentUserId() {
        try {
            Authentication a = SecurityContextHolder.getContext().getAuthentication();
            if (a != null && a.getPrincipal() instanceof Number n) return n.longValue();
        } catch (Exception ignore) {}
        Long fromMdc = parseLong(MDC.get("userId"));
        if (fromMdc != null) return fromMdc;
        return parseLong(getRequestSafe().map(r -> r.getHeader("X-User-Id")).orElse(null));
    }

    private String currentUsername() {
        try {
            Authentication a = SecurityContextHolder.getContext().getAuthentication();
            if (a != null && a.getName() != null) return a.getName();
        } catch (Exception ignore) {}
        return firstNonBlank(MDC.get("operator"),
                getRequestSafe().map(r -> r.getHeader("X-Operator")).orElse(null),
                "system");
    }

    private String currentOperatorId(Long userId) {
        String s = firstNonBlank(MDC.get("operatorId"),
                getRequestSafe().map(r -> r.getHeader("X-Operator-Id")).orElse(null));
        if (!blank(s)) return s;
        return userId == null ? null : String.valueOf(userId);
    }

    private String currentTraceId() {
        return Optional.ofNullable(MDC.get("traceId"))
                .orElse(getRequestSafe().map(r -> r.getHeader("X-Trace-Id")).orElse(""));
    }

    private String clientIp() {
        Optional<HttpServletRequest> opt = getRequestSafe();
        if (opt.isEmpty()) return "";
        HttpServletRequest r = opt.get();
        String xff = r.getHeader("X-Forwarded-For");
        if (!blank(xff)) return xff.split(",")[0].trim();
        String real = r.getHeader("X-Real-IP");
        if (!blank(real)) return real.trim();
        return r.getRemoteAddr();
    }

    private String requestSource() {
        Optional<HttpServletRequest> opt = getRequestSafe();
        return opt.map(r -> r.getRequestURI() + " " + r.getMethod()).orElse(null);
    }

    private Optional<HttpServletRequest> getRequestSafe() {
        // 优先使用 provider 注入；退而求其次用 RequestContextHolder
        HttpServletRequest r = requestProvider.getIfAvailable();
        if (r != null) return Optional.of(r);
        try {
            RequestAttributes ra = RequestContextHolder.getRequestAttributes();
            if (ra instanceof org.springframework.web.context.request.ServletRequestAttributes sra) {
                return Optional.ofNullable(sra.getRequest());
            }
        } catch (Exception ignore) {}
        return Optional.empty();
    }

    /* ======================= 过滤器 & 小工具 ======================= */

    private boolean isSkippablePath() {
        try {
            String uri = getRequestSafe().map(HttpServletRequest::getRequestURI).orElse("");
            if (blank(uri)) return false;
            return uri.startsWith("/actuator")
                    || uri.startsWith("/swagger")
                    || uri.startsWith("/v3/api-docs")
                    || uri.startsWith("/webjars");
        } catch (Exception ignore) {
            return false;
        }
    }

    private static String buildContentJson(String detail, String result, String failReason) {
        StringBuilder sb = new StringBuilder(128);
        sb.append("{\"result\":\"").append(escape(result)).append("\"");
        if (!blank(detail))     sb.append(",\"detail\":").append(jsonQuote(detail));
        if (!blank(failReason)) sb.append(",\"failReason\":").append(jsonQuote(failReason));
        sb.append("}");
        return sb.toString();
    }

    private static String jsonQuote(String s) {
        if (s == null) return "null";
        return "\"" + escape(s) + "\"";
    }
    private static String escape(String s) {
        return s.replace("\\","\\\\").replace("\"","\\\"").replace("\n","\\n").replace("\r","\\r");
    }

    private static String truncate(String s, int max) {
        return (s == null || s.length() <= max) ? s : s.substring(0, max);
    }
    private static String nvl(String s) { return s == null ? "" : s; }
    private static boolean blank(String s) { return s == null || s.trim().isEmpty(); }
    private static Long parseLong(String s) {
        try { return blank(s) ? null : Long.parseLong(s.trim()); } catch (Exception e) { return null; }
    }
    private static String firstNonBlank(String... arr) {
        if (arr == null) return null;
        for (String s : arr) if (!blank(s)) return s;
        return null;
    }
}
