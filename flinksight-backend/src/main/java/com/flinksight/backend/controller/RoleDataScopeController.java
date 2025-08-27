package com.flinksight.backend.controller;

import com.flinksight.backend.common.ApiResponse;
import com.flinksight.common.dto.RoleDataScopeDTO;
import com.flinksight.common.model.PageResult;
import com.flinksight.common.service.RoleDataScopeService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

/**
 * 角色-数据权限分配管理
 */
@Tag(name = "api", description = "角色-数据权限分配管理API")
@RestController
@RequestMapping("/api/role/data-scope")
@RequiredArgsConstructor
@Validated
public class RoleDataScopeController {

    private final RoleDataScopeService service;

    @Operation(summary = "", description = "",operationId = "assignRoleDataScope")
    @PostMapping("/assign")
    public ApiResponse<RoleDataScopeDTO> assign(@RequestParam Long roleId, @RequestParam Long dataScopeId) {
        return ApiResponse.ok(service.assignDataScopeToRole(roleId, dataScopeId));
    }

    @Operation(summary = "", description = "",operationId = "removeRoleDataScope")
    @PostMapping("/remove")
    public boolean remove(@RequestParam Long roleId, @RequestParam Long dataScopeId) {
        return service.removeDataScopeFromRole(roleId, dataScopeId);
    }

    @Operation(summary = "", description = "",operationId = "listRoleDataScopes")
    @GetMapping("/list")
    public ApiResponse<PageResult<RoleDataScopeDTO>> findByDataScope(@RequestParam Long roleId,
                                                                    @RequestParam Long dataScopeId,
                                                                    @RequestParam(defaultValue = "0") int page,
                                                                    @RequestParam(defaultValue = "20") int size) {
        return ApiResponse.ok(service.list(roleId,dataScopeId,page,size));
    }

    @Operation(summary = "删除角色", description = "Delete role by ID",operationId = "getRoleDataScope")
    @GetMapping("/id/{id}")
    public ApiResponse<RoleDataScopeDTO> getById(@PathVariable Long id) {
        return service.getById(id)
                .map(ApiResponse::ok)
                .orElse(ApiResponse.ok(null));
    }

    @Operation(summary = "删除角色", description = "Delete role by ID",operationId = "deleteRoleDataScope")
    @DeleteMapping("/delete/{id}")
    public boolean delete(@PathVariable Long id) {
        return service.sDelete(id);
    }
}
