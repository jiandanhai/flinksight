package com.flinksight.backend.controller;

import com.flinksight.backend.common.ApiResponse;
import com.flinksight.common.dto.RolePermissionDTO;
import com.flinksight.common.model.PageResult;
import com.flinksight.common.service.RolePermissionService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

/**
 * 角色权限分配管理
 */
@Tag(name = "api", description = "角色权限分配管理API")
@RestController
@RequestMapping("/api/role/permission")
@RequiredArgsConstructor
@Validated
public class RolePermissionController {

    private final RolePermissionService service;

    @Operation(summary = "", description = "",operationId = "assignRolePermission")
    @PostMapping("/assign")
    public ApiResponse<RolePermissionDTO> assign(@RequestParam Long roleId, @RequestParam String permissionCode) {
        return ApiResponse.ok(service.assignPermissionToRole(roleId, permissionCode));
    }

    @Operation(summary = "", description = "",operationId = "removeRolePermission")
    @PostMapping("/remove")
    public boolean remove(@RequestParam Long roleId, @RequestParam String permissionCode) {
        return service.removePermissionFromRole(roleId, permissionCode);
    }

    @Operation(summary = "", description = "",operationId = "listRolePermissions")
    @GetMapping("/list")
    public ApiResponse<PageResult<RolePermissionDTO>> list(@RequestParam Long roleId,
                                                                         @RequestParam String permissionCode,
                                                                         @RequestParam(defaultValue = "0") int page,
                                                                         @RequestParam(defaultValue = "20") int size) {
        return ApiResponse.ok(service.list(roleId,permissionCode,page,size));
    }

    @Operation(summary = "", description = "",operationId = "getRolePermission")
    @GetMapping("/id/{id}")
    public ApiResponse<RolePermissionDTO> getById(@PathVariable Long id) {
        return service.getById(id)
                .map(ApiResponse::ok)
                .orElse(ApiResponse.ok(null));
    }

    @Operation(summary = "", description = "",operationId = "deleteRolePermission")
    @DeleteMapping("/delete/{id}")
    public boolean delete(@PathVariable Long id) {
        return service.sDelete(id);
    }
}
