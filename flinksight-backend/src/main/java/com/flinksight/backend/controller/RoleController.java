package com.flinksight.backend.controller;

import com.flinksight.backend.common.ApiResponse;
import com.flinksight.backend.security.tenant.TenantRequired;
import com.flinksight.common.dto.RoleDTO;
import com.flinksight.common.model.PageResult;
import com.flinksight.common.service.RoleService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

/**
 * 角色管理接口
 */
@Tag(name = "角色管理", description = "Role Management API")
@RestController
@RequestMapping("/api/sso/role")
@RequiredArgsConstructor
@TenantRequired
public class RoleController {

    private final RoleService roleService;

    @Operation(summary = "新建角色", description = "Create new role",operationId = "createRole")
    @PostMapping("/create")
    public ApiResponse<RoleDTO> createRole(@RequestBody RoleDTO dto) {
        return ApiResponse.ok(roleService.createRole(dto));
    }

    @Operation(summary = "根据ID查询角色", description = "Get role by ID",operationId = "getRole")
    @GetMapping("/{id}")
    public ApiResponse<RoleDTO> getRoleById(@PathVariable Long id) {
        return roleService.getRoleById(id)
                .map(ApiResponse::ok)
                .orElse(ApiResponse.ok(null));
    }

    @Operation(summary = "查询所有角色", description = "Get all roles",operationId = "getAllRoles")
    @GetMapping("/list")
    public ApiResponse<PageResult<RoleDTO>> getAllRoles(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size) {
        return ApiResponse.ok(roleService.getAllRoles(page,size));
    }

    @Operation(summary = "分页查询角色",operationId = "getRolesByName")
    @GetMapping("/page")
    public ApiResponse<PageResult<RoleDTO>> pageList(
            @RequestParam(required = false) String name,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size) {
        return ApiResponse.ok(roleService.pageList(name, page, size));
    }

    @Operation(summary = "编辑角色",operationId = "updateRole")
    @PutMapping("/update")
    public ApiResponse<RoleDTO> update(@RequestBody RoleDTO dto) {
        return ApiResponse.ok(roleService.update(dto));
    }

    @Operation(summary = "删除角色", description = "Delete role by ID",operationId = "deleteRole")
    @DeleteMapping("/{id}")
    public ApiResponse<Void> deleteRole(@PathVariable Long id) {
        roleService.softDelete(id);
        return ApiResponse.ok(null);
    }
}
