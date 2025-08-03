package com.flinksight.backend.repository;

import com.flinksight.backend.domain.AlertHistory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AlertHistoryRepository extends JpaRepository<AlertHistory, Long> {
    Page<AlertHistory> findByTenantIdAndIsDeleted(Long tenantId, Integer isDeleted, Pageable pageable);
    Page<AlertHistory> findByIsDeleted(Integer isDeleted, Pageable pageable);

}
