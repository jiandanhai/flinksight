package com.flinksight.backend.repository;

import com.flinksight.backend.domain.RolePermission;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.List;

@Repository
public interface RolePermissionRepository extends JpaRepository<RolePermission, Long> {
    Page<RolePermission> findByRoleIdAndIsDeleted(Long roleId, Integer isDeleted, Pageable pageable);

    Page<RolePermission> findByPermissionCodeAndIsDeleted(String permissionCode, Integer isDeleted, Pageable pageable);
    /**
     * 根据角色ID查询角色的所有权限关联
     * @param roleId 角色ID
     * @return 角色权限关系列表
     */
    Page<RolePermission> findByRoleId(Long roleId, Pageable pageable);


    // 根据角色和权限编码查（防止重复插入等）
    List<RolePermission> findByRoleIdAndPermissionCodeAndIsDeleted(Long roleId, String permissionCode, Integer isDeleted);



    @Query("SELECT p.code FROM RolePermission rp JOIN Permission p ON rp.permissionCode = p.code WHERE rp.roleId = :roleId AND rp.isDeleted = :isDeleted")
    List<String> findPermissionCodesByRoleIdAndIsDeleted(@Param("roleId") Long roleId, @Param("isDeleted") Integer isDeleted);

    @Query("SELECT rp.permissionCode FROM RolePermission rp WHERE rp.roleId = :roleId AND rp.isDeleted = 0")
    List<Long> findPermissionCodesByRoleId(@Param("roleId") Long roleId);


    List<RolePermission> findByRoleIdAndTenantIdAndIsDeleted(Long roleId, Long tenantId, Integer isDeleted);

    boolean existsByRoleIdAndPermissionCodeAndTenantIdAndIsDeleted(Long roleId, String code, Long tenantId, Integer isDeleted);

    @Modifying
    @Query("update RolePermission rp set rp.isDeleted=1 where rp.roleId=:roleId and rp.tenantId=:tenantId and rp.permissionCode in :codes")
    int softDeleteByCodes(@Param("roleId") Long roleId, @Param("tenantId") Long tenantId, @Param("codes") Collection<String> codes);
}
