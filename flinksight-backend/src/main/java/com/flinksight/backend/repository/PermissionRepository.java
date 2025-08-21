package com.flinksight.backend.repository;

import com.flinksight.backend.domain.Permission;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

/**
 * 权限点表数据访问接口
 * Permission Repository
 */
@Repository
    public interface PermissionRepository extends JpaRepository<Permission, Long>, SoftDeleteRepository<Permission, Long> {

    List<Permission> findByCodeInAndTenantId(Collection<String> codes, Long tenantId);

    Optional<Permission> findByCodeAndTenantId(String code, Long tenantId);
    /**
     * 根据权限编码查询
     */
    Permission findByCode(String code);

    /**
     * 校验租户是否启用（简化示例，实际应关联租户表判断）
     */
    @Query("SELECT CASE WHEN COUNT(p) > 0 THEN true ELSE false END FROM Permission p WHERE p.tenantId = ?1 AND p.enabled = 1")
    Boolean isTenantEnabled(Long tenantId);

    Page<Permission> findByIsDeleted(Integer isDeleted, Pageable pageable);


    /**
     * 查询用户（可选按租户）拥有的去重权限码。
     * 说明：
     * - 这里用原生 SQL，是因为跨多表且部分表名（role、user）在 MySQL 中为保留词，已用反引号转义。
     * - 当 :tenantId 传 null 时，通过 COALESCE 让条件恒成立，相当于不按租户过滤。
     */
    @Query(value = """
            SELECT DISTINCT p.code
            FROM `user_role` ur
            JOIN `role` r              ON r.id = ur.role_id        AND r.is_deleted = 0
            JOIN `role_permission` rp  ON rp.role_id = r.id        AND rp.is_deleted = 0
            JOIN `permission` p        ON p.code = rp.permission_code  AND p.is_deleted = 0
            WHERE ur.user_id = :userId
              AND ur.is_deleted = 0
              AND (ur.tenant_id = COALESCE(:tenantId, ur.tenant_id))
            """, nativeQuery = true)
    List<String> findCodesByUser(@Param("userId") Long userId,
                                 @Param("tenantId") Long tenantId);


    @Query(value = """
    SELECT DISTINCT rp.permission_code
    FROM user_role ur
    JOIN role r ON r.id = ur.role_id AND r.is_deleted = 0
    JOIN role_permission rp ON rp.role_id = r.id AND rp.is_deleted = 0
    -- 可选：只返回启用的权限码
    JOIN permission p ON p.code = rp.permission_code
                     AND p.tenant_id = COALESCE(:tenantId, ur.tenant_id)
                     AND p.is_deleted = 0
                     AND p.enabled = 1
    WHERE ur.user_id = :userId
      AND ur.is_deleted = 0
      AND (rp.tenant_id = COALESCE(:tenantId, ur.tenant_id))
    """, nativeQuery = true)
    List<String> findCodesByUserAndEnabled(@Param("userId") Long userId,
                                 @Param("tenantId") Long tenantId);


    @Query(value = """
    SELECT COUNT(1)
    FROM user_role ur
    JOIN role_permission rp ON rp.role_id = ur.role_id
                           AND rp.is_deleted = 0
                           AND rp.tenant_id = COALESCE(:tenantId, ur.tenant_id)
    -- 可选：只统计启用的权限，才需要这句
    JOIN permission p ON p.code = rp.permission_code
                     AND p.tenant_id = rp.tenant_id
                     AND p.is_deleted = 0
                     AND p.enabled = 1
    WHERE ur.user_id = :userId
      AND ur.is_deleted = 0
      AND rp.permission_code = :code
    """, nativeQuery = true)
    int userHasPermission(@Param("userId") Long userId,
                          @Param("tenantId") Long tenantId,
                          @Param("code") String permissionCode);

}
