package com.flinksight.backend.repository;

import com.flinksight.backend.domain.Cluster;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
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
    Page<Cluster> findAllByTenantIdAndIsDeleted(Long tenantId, Integer isDeleted, Pageable pageable);
    Page<Cluster> findByIsDeleted(Integer isDeleted, Pageable pageable);

    int countByTenantIdAndIsDeleted(Long tenantId, int isDeleted);
    int countByTenantIdAndStatusAndIsDeleted(Long tenantId, int status, int isDeleted);
    // STATUS_HEALTHY/WARNING/ERROR请在Cluster常量定义
}
