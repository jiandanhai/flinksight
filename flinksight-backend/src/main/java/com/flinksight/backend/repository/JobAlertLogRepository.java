package com.flinksight.backend.repository;

import com.flinksight.backend.domain.JobAlertLog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 作业报警日志仓库
 */
@Repository
public interface JobAlertLogRepository extends JpaRepository<JobAlertLog, Long> {

    /**
     * 查询某租户下的报警记录
     */
    List<JobAlertLog> findByTenantIdAndIsDeleted(Long tenantId, Integer isDeleted);

    /**
     * 查询某作业的全部报警日志
     */
    List<JobAlertLog> findByJobIdAndIsDeleted(Long jobId, Integer isDeleted);

    /**
     * 查询指定类型的报警
     */
    List<JobAlertLog> findByAlertTypeAndIsDeleted(String alertType, Integer isDeleted);

    /**
     * 查询所有未确认（SENT状态）的报警
     */
    List<JobAlertLog> findByStatusAndIsDeleted(String status, Integer isDeleted);

    /**
     * 查询全部未删除报警日志
     */
    List<JobAlertLog> findByIsDeleted(Integer isDeleted);
}
