package com.flinksight.backend.repository;

import com.flinksight.backend.domain.AlertRule;
import com.flinksight.backend.domain.AuditLog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 操作审计日志表数据访问接口
 * AuditLog Repository
 */
@Repository
public interface AuditLogRepository extends JpaRepository<AuditLog, Long> , SoftDeleteRepository<AuditLog, Long>{
    List<AuditLog> findByTenantIdAndUserId(Long tenantId, Long userId);
}
