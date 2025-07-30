package com.flinksight.backend.repository;

import com.flinksight.backend.domain.JobInfo;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

/**
 * 作业注册表Repository，支持幂等查重、权限过滤
 */
public interface JobInfoRepository extends JpaRepository<JobInfo, Long> {
    Optional<JobInfo> findByTraceId(String traceId);
    Optional<JobInfo> findByJobNameAndTenantIdAndIsDeleted(String jobName, Long tenantId, Integer isDeleted);
}
