package com.flinksight.backend.controller;

import com.flinksight.common.service.JobPermissionService;
import com.flinksight.common.dto.JobPermissionDTO;
import com.flinksight.backend.common.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 作业权限分配与查询API
 * 主要用于B端运维/平台租户管理员
 */
@RestController
@RequestMapping("/api/job-permission")
@RequiredArgsConstructor
public class JobPermissionController {

    private final JobPermissionService jobPermissionService;

    /**
     * 查询某作业的所有权限分配记录
     */
    @GetMapping("/list")
    public ApiResponse<List<JobPermissionDTO>> listJobPermissions(@RequestParam Long jobId) {
        List<JobPermissionDTO> list = jobPermissionService.listJobPermissions(jobId);
        return ApiResponse.ok(list);
    }

    /**
     * 分配权限给用户（Owner/Admin/Viewer等）
     */
    @PostMapping("/grant")
    public ApiResponse<Void> grantPermission(
            @RequestParam Long jobId,
            @RequestParam String userId,
            @RequestParam Long permissionId,
            @RequestParam Long tenantId
    ) {
        jobPermissionService.grantPermission(jobId, tenantId, userId, permissionId);
        return ApiResponse.ok(null);
    }

    /**
     * 回收用户权限
     */
    @PostMapping("/revoke")
    public ApiResponse<Void> revokePermission(
            @RequestParam Long jobId,
            @RequestParam String userId,
            @RequestParam Long permissionId
    ) {
        jobPermissionService.revokePermission(jobId, userId, permissionId);
        return ApiResponse.ok(null);
    }

    /**
     * 查询某用户对某作业的权限（Owner/Admin/Viewer等）
     */
    @GetMapping("/user")
    public ApiResponse<List<JobPermissionDTO>> getUserPermissions(
            @RequestParam Long jobId,
            @RequestParam String userId
    ) {
        List<JobPermissionDTO> list = jobPermissionService.getUserPermissions(jobId, userId);
        return ApiResponse.ok(list);
    }
}
