package com.flinksight.backend.repository;

import com.flinksight.backend.domain.RolePermission;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Set;

@Repository
public interface RolePermissionRepository extends JpaRepository<RolePermission, Long> {
    List<RolePermission> findByRoleIdAndIsDeleted(Long roleId, Integer isDeleted);
    List<RolePermission> findByPermissionIdAndIsDeleted(Long permissionId, Integer isDeleted);
    /**
     * 根据角色ID查询角色的所有权限关联
     * @param roleId 角色ID
     * @return 角色权限关系列表
     */
    List<RolePermission> findByRoleId(Long roleId);

    // 也可以补充：批量查
    List<RolePermission> findByRoleIdIn(List<Long> roleIds);

    // 根据角色和权限编码查（防止重复插入等）
    RolePermission findByRoleIdAndPermissionCode(Long roleId, String permissionCode);


    // 根据角色ID查找所有权限ID
    @Query("select rp.permissionId from RolePermission rp where rp.roleId = :roleId and rp.isDeleted = 0")
    List<Long> findPermissionIdsByRoleId(@Param("roleId") Long roleId);

    // 根据权限ID查找所有角色ID
    @Query("select rp.roleId from RolePermission rp where rp.permissionId = :permissionId and rp.isDeleted = 0")
    List<Long> findRoleIdsByPermissionId(@Param("permissionId") Long permissionId);

    // 根据角色ID查找所有权限Code（需关联Permission表）
    @Query("select p.code from RolePermission rp join Permission p on rp.permissionId = p.id where rp.roleId = :roleId and rp.isDeleted = 0 and p.isDeleted = 0")
    List<String> findPermissionCodesByRoleId(@Param("roleId") Long roleId);
}
