package com.flinksight.backend.repository;

import com.flinksight.backend.domain.Permission;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

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


    /**
     * 查询用户（可选按租户）拥有的去重权限码。
     *
     * 说明：
     * - 这里用原生 SQL，是因为跨多表且部分表名（role、user）在 MySQL 中为保留词，已用反引号转义。
     * - 当 :tenantId 传 null 时，通过 COALESCE 让条件恒成立，相当于不按租户过滤。
     */
    @Query(value = """
            SELECT DISTINCT p.code
            FROM `user_role` ur
            JOIN `role` r              ON r.id = ur.role_id        AND r.is_deleted = 0
            JOIN `role_permission` rp  ON rp.role_id = r.id        AND rp.is_deleted = 0
            JOIN `permission` p        ON p.id = rp.permission_id  AND p.is_deleted = 0
            WHERE ur.user_id = :userId
              AND ur.is_deleted = 0
              AND (ur.tenant_id = COALESCE(:tenantId, ur.tenant_id))
            """, nativeQuery = true)
    List<String> findCodesByUser(@Param("userId") Long userId,
                                 @Param("tenantId") Long tenantId);

}
