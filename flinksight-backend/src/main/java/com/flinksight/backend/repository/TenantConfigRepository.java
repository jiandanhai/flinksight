package com.flinksight.backend.repository;

import com.flinksight.backend.domain.TenantConfig;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * 租户配置仓库
 */
@Repository
public interface TenantConfigRepository extends JpaRepository<TenantConfig, Long> {

    /**
     * 根据租户ID和配置key查找（未软删除）
     */
    Optional<TenantConfig> findByTenantIdAndConfigKeyAndIsDeleted(Long tenantId, String configKey, Integer isDeleted);

    /**
     * 获取某租户全部有效配置
     */
    List<TenantConfig> findByTenantIdAndIsDeleted(Long tenantId, Integer isDeleted);

    /**
     * 根据配置key查找所有未删除的配置
     */
    List<TenantConfig> findByConfigKeyAndIsDeleted(String configKey, Integer isDeleted);

    /**
     * 支持批量根据ID查找有效配置
     */
    List<TenantConfig> findByIdInAndIsDeleted(List<Long> ids, Integer isDeleted);

    /**
     * 统计某租户有效配置数
     */
    long countByTenantIdAndIsDeleted(Long tenantId, Integer isDeleted);

    /**
     * 根据租户ID和是否删除查找（分页/排序可扩展）
     */
    List<TenantConfig> findAllByTenantIdAndIsDeleted(Long tenantId, Integer isDeleted);
}
