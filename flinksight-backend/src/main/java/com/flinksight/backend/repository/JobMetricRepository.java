package com.flinksight.backend.repository;

import com.flinksight.backend.domain.JobMetric;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;

/**
 * 任务指标表数据访问接口
 * JobMetric Repository
 */
@Repository
public interface JobMetricRepository extends JpaRepository<JobMetric, Long>, SoftDeleteRepository<JobMetric, Long>  {

    Page<JobMetric> findByJobIdAndMetricTimeBetweenAndIsDeleted(Long jobId, LocalDateTime start, LocalDateTime end, Integer isDeleted, Pageable pageable);

    Page<JobMetric> findByTenantIdAndMetricKeyAndMetricTimeBetweenAndIsDeleted(Long tenantId, String metricKey, LocalDateTime start, LocalDateTime end, Integer isDeleted, Pageable pageable);

    Page<JobMetric> findByTenantIdAndIsDeleted(Long tenantId, Integer isDeleted, Pageable pageable);


}
