package com.flinksight.backend.repository;

import com.flinksight.backend.domain.Cluster;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

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

    List<Cluster> findByTenantIdAndIsDeleted(Long tenantId, Integer isDeleted);

    long countByTenantIdAndIsDeleted(Long tenantId, Integer isDeleted);
    // STATUS_HEALTHY/WARNING/ERROR请在Cluster常量定义

    Optional<Cluster> findByIdAndTenantIdAndIsDeleted(Long id, Long tenantId, Integer isDeleted);

    boolean existsByNameAndTenantIdAndIsDeleted(String name, Long tenantId, Integer isDeleted);

    List<Cluster> findAllByIdInAndTenantIdAndIsDeleted(List<Long> ids, Long tenantId, Integer isDeleted);

    Optional<Cluster> findByIdAndTenantId(Long id, Long tenantId);

    @Modifying
    @Query("update Cluster c set c.status = ?2 where c.id in ?1")
    int updateStatusByIds(List<Long> ids, Integer status);
}
