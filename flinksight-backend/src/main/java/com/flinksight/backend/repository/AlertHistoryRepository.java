package com.flinksight.backend.repository;

import com.flinksight.backend.domain.AlertHistory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface AlertHistoryRepository extends JpaRepository<AlertHistory, Long> {
    List<AlertHistory> findByTenantIdAndIsDeleted(Long tenantId, Integer isDeleted);
}
