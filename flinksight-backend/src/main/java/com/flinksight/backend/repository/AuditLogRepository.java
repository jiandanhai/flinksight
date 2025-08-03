package com.flinksight.backend.repository;

import com.flinksight.backend.domain.AuditLog;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * 操作审计日志表数据访问接口
 * AuditLog Repository
 */
@Repository
public interface AuditLogRepository extends JpaRepository<AuditLog, Long> , SoftDeleteRepository<AuditLog, Long>{
    Page<AuditLog> findByTenantIdAndUserId(Long tenantId, Long userId, Pageable pageable);
}
