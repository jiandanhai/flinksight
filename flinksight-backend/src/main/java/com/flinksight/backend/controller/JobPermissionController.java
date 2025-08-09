package com.flinksight.backend.controller;

import com.flinksight.backend.common.ApiResponse;
import com.flinksight.common.dto.JobPermissionDTO;
import com.flinksight.common.model.PageResult;
import com.flinksight.common.service.JobPermissionService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

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
    @Operation(summary = "", description = "",operationId = "getJobPermissionsByJob")
    @GetMapping("/list")
    public ApiResponse<PageResult<JobPermissionDTO>> listJobPermissions(
            @RequestParam Long jobId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size) {
        return ApiResponse.ok(jobPermissionService.listJobPermissions(jobId,page,size));
    }

    /**
     * 分配权限给用户（Owner/Admin/Viewer等）
     */
    @Operation(summary = "", description = "",operationId = "grantJobPermissionByJobAndUserAndPermissionAndTenant")
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
    @Operation(summary = "", description = "",operationId = "revokePermission")
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
    @Operation(summary = "", description = "",operationId = "getJobPermissionsByJobAndUser")
    @GetMapping("/user")
    public ApiResponse<PageResult<JobPermissionDTO>> getUserPermissions(
            @RequestParam Long jobId,
            @RequestParam String userId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size
    ) {
        return ApiResponse.ok(jobPermissionService.getUserPermissions(jobId,userId,page,size));
    }
}
