package com.flinksight.backend.repository;

import com.flinksight.backend.domain.Permission;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

/**
 * 权限点表数据访问接口
 * Permission Repository
 */
@Repository
    public interface PermissionRepository extends JpaRepository<Permission, Long>, SoftDeleteRepository<Permission, Long> {
    /**
     * 根据权限编码查询
     */
    Permission findByCode(String code);

    /**
     * 校验租户是否启用（简化示例，实际应关联租户表判断）
     */
    @Query("SELECT CASE WHEN COUNT(p) > 0 THEN true ELSE false END FROM Permission p WHERE p.tenantId = ?1 AND p.enabled = 1")
    Boolean isTenantEnabled(Long tenantId);

    /**
     * 校验用户是否拥有某平台级权限
     * 假设有user_permission表可JOIN，也可按业务自定义实现
     */
    @Query(value = "SELECT COUNT(1) FROM user_permission WHERE user_id = ?1 AND permission_id = ?2 AND is_deleted = 0", nativeQuery = true)
    int userHasPermission(Long userId, String permissionId);

    Page<Permission> findByIsDeleted(Integer isDeleted, Pageable pageable);
}
