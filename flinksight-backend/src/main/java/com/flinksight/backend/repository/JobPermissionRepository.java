package com.flinksight.backend.repository;

import com.flinksight.backend.domain.JobPermission;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * 作业-用户-权限三元组 Repository
 * 支持平台权限分配、回收、查询、幂等等商业级业务
 * findByJobId：支持查所有授权用户（权限管理大屏/租户端管理视角）。
 * findByJobIdAndUserId：支持查单用户多权限（用于细粒度授权与回收）。
 * existsByJobIdAndUserIdAndPermissionId：用于幂等性授权校验，避免重复分配。
 * deleteByJobIdAndUserIdAndPermissionId：权限回收，支持管理端、租户端权限剥夺。
 */
public interface JobPermissionRepository extends JpaRepository<JobPermission, Long> {

    /**
     * 查询某作业下的全部权限分配记录
     */
    Page<JobPermission> findByJobIdAndIsDeleted(Long jobId, Integer isDeleted,Pageable pageable);

    Page<JobPermission> findByJobIdAndUserIdAndIsDeleted(Long jobId,String userId, Integer isDeleted,Pageable pageable);

    /**
     * 检查某用户对某作业是否已拥有指定权限（幂等校验）
     */
    boolean existsByJobIdAndUserIdAndPermissionId(Long jobId, String userId, Long permissionId);

    /**
     * 删除某用户对某作业的指定权限（回收权限）
     */
    void deleteByJobIdAndUserIdAndPermissionId(Long jobId, String userId, Long permissionId);

    /**
     * 只要有一条关联即认为有权限（可根据具体角色/权限进一步细化）
     */
    Boolean existsByJobIdAndUserIdAndIsDeleted(Long jobId, String userId, Integer isDeleted);

}
