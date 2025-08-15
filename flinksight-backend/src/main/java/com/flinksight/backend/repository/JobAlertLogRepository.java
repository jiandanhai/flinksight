package com.flinksight.backend.repository;

import com.flinksight.backend.domain.JobAlertLog;
import com.flinksight.common.dto.AlertTrendDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.Instant;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

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

    List<JobAlertLog> findByTenantId(Long tenantId);


    long countByTenantId(Long tenantId);

    List<JobAlertLog> findByTenantIdAndAlertTimeBetween(Long tenantId, LocalDateTime start, LocalDateTime end);

    long countByTenantIdAndAlertTimeBetween(Long tenantId, LocalDateTime start, LocalDateTime end);


    @Query("SELECT new com.flinksight.common.dto.AlertTrendDTO(DATE(j.alertTime), COUNT(j)) " +
            "FROM JobAlertLog j " +
            "WHERE j.tenantId = :tenantId AND j.alertTime BETWEEN :start AND :end " +
            "GROUP BY DATE(j.alertTime) ORDER BY DATE(j.alertTime)")
    List<AlertTrendDTO> aggregateByDay(@Param("tenantId") Long tenantId,
                                       @Param("start") Instant start,
                                       @Param("end") Instant end);

    @Query("SELECT FUNCTION('DATE_FORMAT', a.alertTime, '%Y-%m-%d') AS ts, COUNT(a.id) AS count " +
            "FROM JobAlertLog a " +
            "JOIN JobInstance ji ON a.jobId = ji.jobId " +
            "WHERE a.tenantId = :tenantId AND ji.status = 2 AND a.alertTime BETWEEN :start AND :end " +
            "GROUP BY ts ORDER BY ts")
    List<Map<String, Object>> countFailedJobAlertTrend(@Param("tenantId") Long tenantId,
                                                       @Param("start") LocalDateTime start,
                                                       @Param("end") LocalDateTime end);
}
