package com.flinksight.backend.repository;

import com.flinksight.backend.domain.Job;
import com.flinksight.backend.domain.JobMetric;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 任务表数据访问接口
 * Job Repository
 */
@Repository
public interface JobRepository extends JpaRepository<Job, Long>, SoftDeleteRepository<Job, Long> {
    List<Job> findAllByTenantIdAndClusterIdAndIsDeleted(Long tenantId, Long clusterId, Integer isDeleted);
    List<Job> findAllByTenantIdAndIsDeleted(Long tenantId, Integer isDeleted);
}
