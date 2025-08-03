package com.flinksight.backend.repository;

import com.flinksight.backend.domain.UserRole;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Set;

@Repository
public interface UserRoleRepository extends JpaRepository<UserRole, Long> {
    Page<UserRole> findByUserIdAndIsDeleted(Long userId, Integer isDeleted, Pageable pageable);
    Page<UserRole> findByRoleIdAndIsDeleted(Long roleId, Integer isDeleted, Pageable pageable);
    Page<UserRole> findByTenantIdAndIsDeleted(Long tenantId, Integer isDeleted, Pageable pageable);

    Set<Long> findRoleIdsByUserIdAndIsDeleted(Long userId, Integer isDeleted);
    void deleteByUserIdAndRoleId(Long userId, Long roleId);

    /**
     * 根据用户ID查询所有角色关系
     * @param userId 用户ID
     * @return 用户角色关系列表
     */
    Page<UserRole> findByUserId(Long userId, Pageable pageable);

    // 扩展：批量用户
    Page<UserRole> findByUserIdIn(List<Long> userIds, Pageable pageable);

    List<UserRole> findByUserIdAndRoleIdAndIsDeleted(Long userId, Long roleId, Integer isDeleted);

    // 查询用户在某租户下所有角色（多租户场景）
    Page<UserRole> findByUserIdAndTenantId(Long userId, Long tenantId, Pageable pageable);

    @Query("SELECT ur.roleId FROM UserRole ur WHERE ur.userId = :userId AND ur.isDeleted = 0")
    List<Long> findRoleIdsByUserId(@Param("userId") Long userId);

}
