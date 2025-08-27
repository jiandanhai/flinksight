package com.flinksight.backend.repository;

import com.flinksight.backend.domain.AuditLog;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;

/**
 * 操作审计日志表数据访问接口
 * AuditLog Repository
 */
@Repository
public interface AuditLogRepository extends JpaRepository<AuditLog, Long>,SoftDeleteRepository<AuditLog, Long>{
    Page<AuditLog> findByTenantIdAndUserId(Long tenantId, Long userId, Pageable pageable);

    @Query("""
        SELECT l FROM AuditLog l
         WHERE l.tenantId = :tenantId
           AND (:action     IS NULL OR l.action = :action)
           AND (:targetType IS NULL OR l.targetType = :targetType)
           AND (:targetId   IS NULL OR l.targetId = :targetId)
           AND (:userId     IS NULL OR l.userId = :userId)
           AND (:operator   IS NULL OR l.operator = :operator)
           AND (:traceId    IS NULL OR l.traceId = :traceId)
           AND (:fromTime   IS NULL OR l.createdAt >= :fromTime)
           AND (:toTime     IS NULL OR l.createdAt <= :toTime)
         ORDER BY l.createdAt DESC, l.id DESC
    """)
    Page<AuditLog> pageQuery(@Param("tenantId") Long tenantId,
                             @Param("action") String action,
                             @Param("targetType") String targetType,
                             @Param("targetId") Long targetId,
                             @Param("userId") Long userId,
                             @Param("operator") String operator,
                             @Param("traceId") String traceId,
                             @Param("fromTime") LocalDateTime fromTime,
                             @Param("toTime") LocalDateTime toTime,
                             Pageable pageable);
}
