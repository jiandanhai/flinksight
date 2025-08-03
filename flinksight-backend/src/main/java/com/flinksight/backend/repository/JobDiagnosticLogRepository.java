package com.flinksight.backend.repository;

import com.flinksight.backend.domain.JobDiagnosticLog;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface JobDiagnosticLogRepository extends JpaRepository<JobDiagnosticLog, Long> {
    Page<JobDiagnosticLog> findByJobIdAndIsDeleted(Long jobId, Integer isDeleted, Pageable pageable);

    Page<JobDiagnosticLog> findByLevelAndIsDeleted(String level, Integer isDeleted, Pageable pageable);
}
