package com.flinksight.backend.controller;

import com.flinksight.backend.common.ApiResponse;
import com.flinksight.common.dto.UserRoleDTO;
import com.flinksight.common.model.PageResult;
import com.flinksight.common.service.UserRoleService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

/**
 * 用户角色分配管理
 */
@Tag(name = "api", description = "用户角色分配管理API")
@RestController
@RequestMapping("/api/user-role")
@RequiredArgsConstructor
public class UserRoleController {

    private final UserRoleService service;

    @Operation(summary = "用户分配角色", description = "",operationId = "assignUserRole")
    @PostMapping("/assign")
    public ApiResponse<UserRoleDTO> assign(@RequestParam Long userId, @RequestParam Long roleId, @RequestParam(required = false) Long tenantId) {
        return ApiResponse.ok(service.assignRoleToUser(userId, roleId, tenantId));
    }

    @Operation(summary = "用户移除角色", description = "",operationId = "removeUserRole")
    @PostMapping("/remove")
    public boolean remove(@RequestParam Long userId, @RequestParam Long roleId) {
        return service.removeRoleFromUser(userId, roleId);
    }

    @Operation(summary = "获取用户所有角色", description = "",operationId = "getUserRolesByUser")
    @GetMapping("/by-user/{userId}")
    public ApiResponse<PageResult<UserRoleDTO>> findRolesByUser(@PathVariable Long userId,
                                                               @RequestParam(defaultValue = "0") int page,
                                                               @RequestParam(defaultValue = "20") int size) {

        return ApiResponse.ok(service.findRolesByUserId(userId,page,size));
    }

    @Operation(summary = "获取角色下所有用户", description = "",operationId = "getUserRolesByRole")
    @GetMapping("/by-role/{roleId}")
    public ApiResponse<PageResult<UserRoleDTO>> findUsersByRole(@PathVariable Long roleId,
                                             @RequestParam(defaultValue = "0") int page,
                                             @RequestParam(defaultValue = "20") int size) {
        return ApiResponse.ok(service.findUsersByRoleId(roleId,page,size));
    }

    @Operation(summary = "", description = "",operationId = "getUserRolesByTenant")
    @GetMapping("/by_tenant/{tenantId}")
    public ApiResponse<PageResult<UserRoleDTO>> findByTenantId(@PathVariable Long tenantId,
                                            @RequestParam(defaultValue = "0") int page,
                                            @RequestParam(defaultValue = "20") int size) {
        return ApiResponse.ok(service.findByTenantId(tenantId,page,size));
    }

    @Operation(summary = "", description = "",operationId = "getUserRole")
    @GetMapping("/{id}")
    public ApiResponse<UserRoleDTO> getById(@PathVariable Long id) {
        return service.getById(id)
                .map(ApiResponse::ok)
                .orElse(ApiResponse.ok(null));
    }

    @Operation(summary = "", description = "",operationId = "deleteUserRole")
    @DeleteMapping("/{id}")
    public boolean delete(@PathVariable Long id) {
        return service.softDelete(id);
    }
}
