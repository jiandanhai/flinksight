package com.flinksight.backend.repository;

import com.flinksight.backend.domain.JobLog;
import com.flinksight.backend.domain.JobMetric;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 任务指标表数据访问接口
 * JobMetric Repository
 */
@Repository
public interface JobMetricRepository extends JpaRepository<JobMetric, Long>, SoftDeleteRepository<JobMetric, Long>  {

    List<JobMetric> findByJobIdAndTsBetweenAndIsDeleted(Long jobId, LocalDateTime start, LocalDateTime end, Integer isDeleted);

    List<JobMetric> findByTenantIdAndMetricKeyAndTsBetweenAndIsDeleted(Long tenantId, String metricKey, LocalDateTime start, LocalDateTime end, Integer isDeleted);

    List<JobMetric> findByTenantIdAndIsDeleted(Long tenantId, Integer isDeleted);


}
