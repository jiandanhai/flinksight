package com.flinksight.backend.repository;

import com.flinksight.backend.domain.JobAlertLog;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * 作业报警日志仓库
 */
@Repository
public interface JobAlertLogRepository extends JpaRepository<JobAlertLog, Long> {

    /**
     * 查询某租户下的报警记录
     */
    Page<JobAlertLog> findByTenantIdAndIsDeleted(Long tenantId, Integer isDeleted, Pageable pageable);

    /**
     * 查询某作业的全部报警日志
     */
    Page<JobAlertLog> findByJobIdAndIsDeleted(Long jobId, Integer isDeleted, Pageable pageable);

    /**
     * 查询指定类型的报警
     */
    Page<JobAlertLog> findByAlertTypeAndIsDeleted(String alertType, Integer isDeleted, Pageable pageable);

    /**
     * 查询所有未确认（SENT状态）的报警
     */
    Page<JobAlertLog> findByStatusAndIsDeleted(String status, Integer isDeleted, Pageable pageable);

    /**
     * 查询全部未删除报警日志
     */
    Page<JobAlertLog> findByIsDeleted(Integer isDeleted, Pageable pageable);
}
