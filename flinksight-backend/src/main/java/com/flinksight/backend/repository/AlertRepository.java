package com.flinksight.backend.repository;

import com.flinksight.backend.domain.Alert;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 报警事件表数据访问接口
 * Alert Repository
 */
@Repository
public interface AlertRepository extends JpaRepository<Alert, Long> , SoftDeleteRepository<Alert, Long> {

    List<Alert> findByTenantIdAndStatusAndIsDeleted(Long tenantId, Integer status, Integer isDeleted);

    List<Alert> findByJobIdAndStatusAndIsDeleted(Long jobId, Integer status, Integer isDeleted);
}
