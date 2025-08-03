package com.flinksight.backend.repository;

import com.flinksight.backend.domain.MetricDashboard;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MetricDashboardRepository extends JpaRepository<MetricDashboard, Long> {
    Page<MetricDashboard> findByTenantIdAndIsDeleted(Long tenantId, Integer isDeleted, Pageable pageable);
    Page<MetricDashboard> findByIsDeleted(Integer isDeleted, Pageable pageable);
}
