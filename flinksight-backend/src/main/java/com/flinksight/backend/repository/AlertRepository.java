package com.flinksight.backend.repository;

import com.flinksight.backend.domain.Alert;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * 报警事件表数据访问接口
 * Alert Repository
 */
@Repository
public interface AlertRepository extends JpaRepository<Alert, Long> , SoftDeleteRepository<Alert, Long> {

    Page<Alert> findByTenantIdAndStatusAndIsDeleted(Long tenantId, Integer status, Integer isDeleted, Pageable pageable);

    Page<Alert> findByJobIdAndStatusAndIsDeleted(Long jobId, Integer status, Integer isDeleted, Pageable pageable);
}
