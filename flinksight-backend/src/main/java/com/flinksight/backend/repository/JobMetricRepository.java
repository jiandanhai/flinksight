package com.flinksight.backend.repository;

import com.flinksight.backend.domain.JobMetric;
import com.flinksight.common.dto.JobMetricDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 任务指标表数据访问接口
 * JobMetric Repository
 */
@Repository
public interface JobMetricRepository extends JpaRepository<JobMetric, Long>, SoftDeleteRepository<JobMetric, Long>  {

    Page<JobMetric> findByJobIdAndMetricTimeBetweenAndIsDeleted(Long tenantId, Long jobId, LocalDateTime start, LocalDateTime end, Integer isDeleted, Pageable pageable);

    Page<JobMetric> findByTenantIdAndMetricKeyAndMetricTimeBetweenAndIsDeleted(Long tenantId, String metricKey, LocalDateTime start, LocalDateTime end, Integer isDeleted, Pageable pageable);

    Page<JobMetric> findByTenantIdAndIsDeleted(Long tenantId, Integer isDeleted, Pageable pageable);

    @Query("""
    SELECT jm FROM JobMetric jm
     WHERE jm.isDeleted = 0
       AND jm.tenantId = :tenantId
       AND (:jobId     IS NULL OR jm.jobId     = :jobId)
       AND (:metricKey IS NULL OR jm.metricKey = :metricKey)
       AND (:start     IS NULL OR jm.metricTime >= :start)
       AND (:end       IS NULL OR jm.metricTime <= :end)
  """)
    Page<JobMetric> pageQuery(@Param("tenantId") Long tenantId,
                              @Param("jobId") Long jobId,
                              @Param("metricKey") String metricKey,
                              @Param("start") LocalDateTime start,
                              @Param("end") LocalDateTime end,
                              Pageable pageable);

    @Query(value = """
        SELECT 
          tenant_id      AS tenantId,
          metric_key     AS metricKey,
          -- 统一到分钟：把 DATETIME 转成 ISO 字符串再由驱动映射为 Instant
          DATE_FORMAT(metric_time, '%Y-%m-%d %H:%i:00') AS ts,
          AVG(metric_value) AS value
        FROM job_metric
        WHERE tenant_id = :tenantId
          AND metric_key = :metricKey
          AND metric_time BETWEEN :from AND :to
          AND is_deleted = 0
        GROUP BY tenant_id, metric_key, ts
        ORDER BY ts
        """, nativeQuery = true)
    List<JobMetricDTO> findSeries(
            @Param("tenantId") Long tenantId,
            @Param("metricKey") String metricKey,
            @Param("from") LocalDateTime from,
            @Param("to") LocalDateTime to
    );

}
