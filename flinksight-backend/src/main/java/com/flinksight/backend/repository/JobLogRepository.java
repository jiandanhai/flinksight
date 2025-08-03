package com.flinksight.backend.repository;

import com.flinksight.backend.domain.JobLog;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;

/**
 * 任务日志表数据访问接口
 * JobLog Repository
 */
@Repository
public interface JobLogRepository extends JpaRepository<JobLog, Long>, SoftDeleteRepository<JobLog, Long>  {

    Page<JobLog> findByJobIdAndLogTimeBetweenAndIsDeleted(Long jobId, LocalDateTime start, LocalDateTime end, Integer isDeleted, Pageable pageable);

    Page<JobLog> findByTenantIdAndLevelAndLogTimeBetweenAndIsDeleted(Long tenantId, String level, LocalDateTime start, LocalDateTime end, Integer isDeleted, Pageable pageable);
}
