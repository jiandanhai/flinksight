package com.flinksight.backend.audit;

import com.flinksight.backend.domain.AuditLog;
import com.flinksight.backend.repository.AuditLogRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionalEventListener;

import java.time.LocalDateTime;

@Component
@RequiredArgsConstructor
@Slf4j
public class AuditLogEventListener {

    private final AuditLogRepository repository;

    /** 仅在外层事务提交后写库，避免失败脏日志 */
    @TransactionalEventListener
    public void on(AuditLogEvent evt) {
        AuditLog e = AuditLog.builder()
                .tenantId(evt.getTenantId())
                .userId(evt.getUserId())
                .operator(evt.getOperator())
                .action(evt.getAction())
                .targetType(evt.getTargetType())
                .targetId(evt.getTargetId())
                .ip(evt.getIp())
                .traceId(evt.getTraceId())
                .content(truncate(evt.getContent(), 8000))
                .createdAt(LocalDateTime.now())
                .isDeleted(0)
                .build();
        repository.save(e);
        log.info("[Audit] saved tenant={}, action={}, target={}#{} by {}({}) trace={}",
                e.getTenantId(), e.getAction(), e.getTargetType(), e.getTargetId(),
                e.getOperator(), e.getUserId(), e.getTraceId());
    }

    private static String truncate(String s, int max) {
        return (s == null || s.length() <= max) ? s : s.substring(0, max);
    }
}