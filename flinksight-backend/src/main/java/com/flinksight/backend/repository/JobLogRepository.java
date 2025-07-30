package com.flinksight.backend.repository;

import com.flinksight.backend.domain.Cluster;
import com.flinksight.backend.domain.JobLog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 任务日志表数据访问接口
 * JobLog Repository
 */
@Repository
public interface JobLogRepository extends JpaRepository<JobLog, Long>, SoftDeleteRepository<JobLog, Long>  {

    List<JobLog> findByJobIdAndTsBetweenAndIsDeleted(Long jobId, LocalDateTime start, LocalDateTime end, Integer isDeleted);

    List<JobLog> findByTenantIdAndLevelAndTsBetweenAndIsDeleted(Long tenantId, String level, LocalDateTime start, LocalDateTime end, Integer isDeleted);
}
