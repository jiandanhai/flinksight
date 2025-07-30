package com.flinksight.backend.repository;

import com.flinksight.backend.domain.JobDiagnosticLog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface JobDiagnosticLogRepository extends JpaRepository<JobDiagnosticLog, Long> {
    List<JobDiagnosticLog> findByJobIdAndIsDeleted(Long jobId, Integer isDeleted);

    List<JobDiagnosticLog> findByLevelAndIsDeleted(String level, Integer isDeleted);
}
