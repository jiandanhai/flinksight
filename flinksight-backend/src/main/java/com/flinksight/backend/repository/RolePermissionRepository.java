package com.flinksight.backend.repository;

import com.flinksight.backend.domain.RolePermission;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RolePermissionRepository extends JpaRepository<RolePermission, Long> {
    Page<RolePermission> findByRoleIdAndIsDeleted(Long roleId, Integer isDeleted, Pageable pageable);
    Page<RolePermission> findByPermissionIdAndIsDeleted(Long permissionId, Integer isDeleted, Pageable pageable);
    /**
     * 根据角色ID查询角色的所有权限关联
     * @param roleId 角色ID
     * @return 角色权限关系列表
     */
    Page<RolePermission> findByRoleId(Long roleId, Pageable pageable);

    // 也可以补充：批量查
    Page<RolePermission> findByRoleIdIn(List<Long> roleIds, Pageable pageable);

    // 根据角色和权限编码查（防止重复插入等）
    List<RolePermission> findByRoleIdAndPermissionIdAndIsDeleted(Long roleId, Long permissionId, Integer isDeleted);


    // 根据角色ID查找所有权限ID
    @Query("select rp.permissionId from RolePermission rp where rp.roleId = :roleId and rp.isDeleted = 0")
    Page<Long> findPermissionIdsByRoleId(@Param("roleId") Long roleId, Pageable pageable);

    // 根据权限ID查找所有角色ID
    @Query("select rp.roleId from RolePermission rp where rp.permissionId = :permissionId and rp.isDeleted = 0")
    Page<Long> findRoleIdsByPermissionId(@Param("permissionId") Long permissionId, Pageable pageable);


    @Query("SELECT p.code FROM RolePermission rp JOIN Permission p ON rp.permissionId = p.id WHERE rp.roleId = :roleId AND rp.isDeleted = :isDeleted")
    List<String> findPermissionCodesByRoleIdAndIsDeleted(@Param("roleId") Long roleId,
                                                                @Param("isDeleted") Integer isDeleted
                                                                );

    @Query("SELECT rp.permissionId FROM RolePermission rp WHERE rp.roleId = :roleId AND rp.isDeleted = 0")
    List<Long> findPermissionIdsByRoleId(@Param("roleId") Long roleId);
}
