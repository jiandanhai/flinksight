package com.flinksight.backend.controller;

import com.flinksight.backend.common.ApiResponse;
import com.flinksight.common.dto.RolePermissionDTO;
import com.flinksight.common.model.PageResult;
import com.flinksight.common.service.RolePermissionService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

/**
 * 角色权限分配管理
 */
@Tag(name = "api", description = "角色权限分配管理API")
@RestController
@RequestMapping("/api/role-permission")
@RequiredArgsConstructor
public class RolePermissionController {

    private final RolePermissionService service;

    @Operation(summary = "", description = "",operationId = "assignRolePermission")
    @PostMapping("/assign")
    public ApiResponse<RolePermissionDTO> assign(@RequestParam Long roleId, @RequestParam Long permissionId) {
        return ApiResponse.ok(service.assignPermissionToRole(roleId, permissionId));
    }

    @Operation(summary = "", description = "",operationId = "removeRolePermission")
    @PostMapping("/remove")
    public boolean remove(@RequestParam Long roleId, @RequestParam Long permissionId) {
        return service.removePermissionFromRole(roleId, permissionId);
    }

    @Operation(summary = "", description = "",operationId = "getRolePermissionsByRole")
    @GetMapping("/role/{roleId}")
    public ApiResponse<PageResult<RolePermissionDTO>> findByRoleId(@PathVariable Long roleId,
                                                                  @RequestParam(defaultValue = "0") int page,
                                                                  @RequestParam(defaultValue = "20") int size) {

        return ApiResponse.ok(service.findByRoleId(roleId,page,size));
    }

    @Operation(summary = "", description = "",operationId = "getRolePermissionsByPermission")
    @GetMapping("/permission/{permissionId}")
    public ApiResponse<PageResult<RolePermissionDTO>> findByPermissionId(@PathVariable Long permissionId,
                                                      @RequestParam(defaultValue = "0") int page,
                                                      @RequestParam(defaultValue = "20") int size) {
        return ApiResponse.ok(service.findByPermissionId(permissionId,page,size));
    }

    @Operation(summary = "", description = "",operationId = "getRolePermission")
    @GetMapping("/{id}")
    public ApiResponse<RolePermissionDTO> getById(@PathVariable Long id) {
        return service.getById(id)
                .map(ApiResponse::ok)
                .orElse(ApiResponse.ok(null));
    }

    @Operation(summary = "", description = "",operationId = "deleteRolePermission")
    @DeleteMapping("/{id}")
    public boolean delete(@PathVariable Long id) {
        return service.softDelete(id);
    }
}
