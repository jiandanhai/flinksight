package com.flinksight.backend.repository;

import com.flinksight.backend.domain.JobLog;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;

/**
 * 任务日志表数据访问接口
 * JobLog Repository
 */
@Repository
public interface JobLogRepository extends JpaRepository<JobLog, Long>, SoftDeleteRepository<JobLog, Long>  {

    Page<JobLog> findByJobIdAndLogTimeBetweenAndIsDeleted(Long tenantId,Long jobId, LocalDateTime start, LocalDateTime end, Integer isDeleted, Pageable pageable);

    Page<JobLog> findByTenantIdAndLevelAndLogTimeBetweenAndIsDeleted(Long tenantId, String level, LocalDateTime start, LocalDateTime end, Integer isDeleted, Pageable pageable);


    @Query("""
    SELECT jl FROM JobLog jl
     WHERE jl.isDeleted = 0
       AND jl.tenantId = :tenantId
       AND (:jobId IS NULL OR jl.jobId = :jobId)
       AND (:level IS NULL OR jl.level = :level)
       AND (:start IS NULL OR jl.logTime >= :start)
       AND (:end   IS NULL OR jl.logTime <= :end)
  """)
    Page<JobLog> pageQuery(@Param("tenantId") Long tenantId,
                           @Param("jobId")    Long jobId,
                           @Param("level")    String level,
                           @Param("start")    LocalDateTime start,
                           @Param("end")      LocalDateTime end,
                           Pageable pageable);
}
