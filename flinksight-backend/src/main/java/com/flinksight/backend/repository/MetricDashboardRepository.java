package com.flinksight.backend.repository;

import com.flinksight.backend.domain.MetricDashboard;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface MetricDashboardRepository extends JpaRepository<MetricDashboard, Long> {
    List<MetricDashboard> findByTenantIdAndIsDeleted(Long tenantId, Integer isDeleted);
}
