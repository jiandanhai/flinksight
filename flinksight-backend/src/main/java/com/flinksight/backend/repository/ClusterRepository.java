package com.flinksight.backend.repository;

import com.flinksight.backend.domain.AuditLog;
import com.flinksight.backend.domain.Cluster;
import com.flinksight.common.dto.ClusterDTO;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 集群表数据访问接口
 * Cluster Repository
 */
@Repository
public interface ClusterRepository extends JpaRepository<Cluster, Long>, SoftDeleteRepository<Cluster, Long> {
    List<Cluster> findByStatusAndIsDeleted(Integer status, Integer isDeleted);
    List<Cluster> findAllByTenantIdAndIsDeleted(Long tenantId, Integer isDeleted);
}
