package com.flinksight.backend.repository;

import com.flinksight.backend.domain.NodeHealth;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * 节点健康状态仓库
 */
@Repository
public interface NodeHealthRepository extends JpaRepository<NodeHealth, Long> {

    /**
     * 查找所有某租户下的未删除节点健康记录
     */
    Page<NodeHealth> findByTenantIdAndIsDeleted(Long tenantId, Integer isDeleted, Pageable pageable);

    /**
     * 根据节点ID查询最新的健康状态
     */
    Optional<NodeHealth> findTopByNodeIdAndIsDeletedOrderByCheckTimeDesc(Long nodeId, Integer isDeleted);

    /**
     * 查询某节点所有历史健康记录
     */
    Page<NodeHealth> findByNodeIdAndIsDeleted(Long nodeId, Integer isDeleted, Pageable pageable);

    /**
     * 批量根据ID查找未删除的健康记录
     */
    List<NodeHealth> findByIdInAndIsDeleted(List<Long> ids, Integer isDeleted);

    /**
     * 统计某节点的健康异常数
     */
    long countByNodeIdAndHealthStatusAndIsDeleted(Long nodeId, String healthStatus, Integer isDeleted);
}
