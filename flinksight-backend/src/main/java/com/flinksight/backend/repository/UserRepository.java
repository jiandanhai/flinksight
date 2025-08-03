package com.flinksight.backend.repository;

import com.flinksight.backend.domain.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * 用户表数据访问接口
 * User Repository
 */
@Repository
public interface UserRepository extends JpaRepository<User, Long>, SoftDeleteRepository<User, Long> {

    /**
     * 根据用户名和租户ID查找用户（未被软删除）
     * @param tenantId 租户ID
     * @param username 用户名
     * @param isDeleted 软删除标志
     * @return 用户对象
     */
    Optional<User> findByTenantIdAndUsernameAndIsDeleted(Long tenantId, String username, Integer isDeleted);

    /**
     * 查询指定租户下所有未删除用户
     * @param tenantId 租户ID
     * @param isDeleted 软删除标志
     * @return 用户列表
     */
    Page<User> findAllByTenantIdAndIsDeleted(Long tenantId, Integer isDeleted, Pageable pageable);

    /**
     * 根据用户名和租户ID查询用户（用于多租户场景，支持软删除过滤）
     * @param username 用户名
     * @param tenantId 租户ID
     * @param isDeleted 软删除标志
     * @return 用户实体
     */
    Optional<User> findByUsernameAndTenantIdAndIsDeleted(String username, Long tenantId, Integer isDeleted);

    // 兼容单租户，或不区分 isDeleted
    Optional<User> findByUsernameAndTenantId(String username, Long tenantId);

    // 根据用户名查找用户（不区分租户，仅用于超级管理员等场景）
    Optional<User> findByUsername(String username);

    // 按用户名和未删除状态查找用户（isDeleted = 0/false）
    Optional<User> findByUsernameAndIsDeleted(String username, Integer isDeleted);
}
