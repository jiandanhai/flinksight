package com.flinksight.backend.repository;

import com.flinksight.backend.domain.Alert;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 报警事件表数据访问接口
 * Alert Repository
 */
@Repository
public interface AlertRepository extends JpaRepository<Alert, Long> , SoftDeleteRepository<Alert, Long> {

    Page<Alert> findByTenantIdAndStatusAndIsDeleted(Long tenantId, Integer status, Integer isDeleted, Pageable pageable);

    Page<Alert> findByJobIdAndStatusAndIsDeleted(Long jobId, Integer status, Integer isDeleted, Pageable pageable);

    Page<Alert> findByLevelAndStatusAndIsDeleted(String level, Integer status, Integer isDeleted, Pageable pageable);

    int countByTenantIdAndIsDeleted(Long tenantId, int isDeleted);

    long countByTenantIdAndCreatedAtBetween(Long tenantId, LocalDateTime from, LocalDateTime to);

    long countByTenantIdAndLevelAndCreatedAtBetween(Long tenantId, String level, LocalDateTime from, LocalDateTime to);

    long countByTenantIdAndStatusAndCreatedAtBetween(Long tenantId, Integer status, LocalDateTime start, LocalDateTime end);

    List<Alert> findByTenantIdAndCreatedAtBetween(Long tenantId, LocalDateTime start, LocalDateTime end);

    @Query("select a from Alert a where a.tenantId = ?1 and a.createdAt between ?2 and ?3 order by a.createdAt asc")
    List<Alert> findTrend(Long tenantId, LocalDateTime from, LocalDateTime to);

    List<Alert> findByTenantIdAndIsDeletedFalse(Long tenantId);
}

