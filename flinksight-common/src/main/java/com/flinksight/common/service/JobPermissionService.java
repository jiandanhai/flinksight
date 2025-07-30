package com.flinksight.common.service;

import com.flinksight.common.dto.JobPermissionDTO;
import java.util.List;

/**
 * 作业-用户-权限三元组关系服务
 * 管理所有具体授权、回收、查找
 */
public interface JobPermissionService {

    /**
     * 注册新作业时分配默认权限给操作人/租户（如OWNER/ADMIN）
     * 典型实现：插入job_permission三元组
     */
    void grantDefaultJobPermission(Long jobId, Long tenantId, String operatorUserId);

    /**
     * 查询某作业下所有授权用户
     */
    List<JobPermissionDTO> listJobPermissions(Long jobId);

    /**
     * 给指定用户分配作业权限
     */
    void grantPermission(Long jobId, Long tenantId, String userId, Long permissionId);

    /**
     * 回收用户的作业权限
     */
    void revokePermission(Long jobId, String userId, Long permissionId);

    /**
     * 校验指定用户是否拥有某作业的管理权限
     */
    boolean hasJobPermission(Long jobId, Long userId);

    /**
     * 查询某用户对某作业的所有权限
     */
    List<JobPermissionDTO> getUserPermissions(Long jobId, String userId);
}
