package com.flinksight.backend.repository;

import com.flinksight.backend.domain.RolePermission;
import com.flinksight.backend.domain.UserRole;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Set;

@Repository
public interface UserRoleRepository extends JpaRepository<UserRole, Long> {
    List<UserRole> findByUserIdAndIsDeleted(Long userId, Integer isDeleted);
    List<UserRole> findByRoleIdAndIsDeleted(Long roleId, Integer isDeleted);
    List<UserRole> findByTenantIdAndIsDeleted(Long tenantId, Integer isDeleted);
    @Query("select ur.roleId from UserRole ur where ur.userId = :userId and ur.isDeleted = 0")
    Set<Long> findRoleIdsByUserId(@Param("userId") Long userId);
    void deleteByUserIdAndRoleId(Long userId, Long roleId);

    /**
     * 根据用户ID查询所有角色关系
     * @param userId 用户ID
     * @return 用户角色关系列表
     */
    List<UserRole> findByUserId(Long userId);

    // 扩展：批量用户
    List<UserRole> findByUserIdIn(List<Long> userIds);

    // 检查指定用户是否拥有某角色
    UserRole findByUserIdAndRoleId(Long userId, Long roleId);

    // 查询用户在某租户下所有角色（多租户场景）
    List<UserRole> findByUserIdAndTenantId(Long userId, Long tenantId);

}
