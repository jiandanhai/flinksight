package com.flinksight.backend.repository;

import com.flinksight.backend.domain.JobDiagnosticLog;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface JobDiagnosticLogRepository extends JpaRepository<JobDiagnosticLog, Long> {

    @Query("""
    SELECT l FROM JobDiagnosticLog l
    WHERE l.isDeleted = 0
      AND (:jobId IS NULL OR l.jobId = :jobId)
      AND (:level IS NULL OR l.level = :level)
  """)
    Page<JobDiagnosticLog> pageQuery(@Param("jobId") Long jobId,
                                     @Param("level") String level,
                                     Pageable pageable);
}
