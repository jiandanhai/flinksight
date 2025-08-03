package com.flinksight.backend.repository;

import com.flinksight.backend.domain.Job;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * 任务表数据访问接口
 * Job Repository
 */
@Repository
public interface JobRepository extends JpaRepository<Job, Long>, SoftDeleteRepository<Job, Long> {
    Page<Job> findAllByTenantIdAndClusterIdAndIsDeleted(Long tenantId, Long clusterId, Integer isDeleted, Pageable pageable);
    Page<Job> findAllByTenantIdAndIsDeleted(Long tenantId, Integer isDeleted, Pageable pageable);
}
