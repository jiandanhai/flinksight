package com.flinksight.backend.controller;

import com.flinksight.backend.common.ApiResponse;
import com.flinksight.common.dto.RolePermissionDTO;
import com.flinksight.common.model.PageResult;
import com.flinksight.common.service.RolePermissionService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

/**
 * 角色权限分配管理
 */
@RestController
@RequestMapping("/api/role-permission")
@RequiredArgsConstructor
public class RolePermissionController {

    private final RolePermissionService service;

    @PostMapping("/assign")
    public ApiResponse<RolePermissionDTO> assign(@RequestParam Long roleId, @RequestParam Long permissionId) {
        return ApiResponse.ok(service.assignPermissionToRole(roleId, permissionId));
    }

    @PostMapping("/remove")
    public boolean remove(@RequestParam Long roleId, @RequestParam Long permissionId) {
        return service.removePermissionFromRole(roleId, permissionId);
    }

    @GetMapping("/role/{roleId}")
    public ApiResponse<PageResult<RolePermissionDTO>> findByRoleId(@PathVariable Long roleId,
                                                                  @RequestParam(defaultValue = "0") int page,
                                                                  @RequestParam(defaultValue = "20") int size) {

        return ApiResponse.ok(service.findByRoleId(roleId,page,size));
    }

    @GetMapping("/permission/{permissionId}")
    public ApiResponse<PageResult<RolePermissionDTO>> findByPermissionId(@PathVariable Long permissionId,
                                                      @RequestParam(defaultValue = "0") int page,
                                                      @RequestParam(defaultValue = "20") int size) {
        return ApiResponse.ok(service.findByPermissionId(permissionId,page,size));
    }

    @GetMapping("/{id}")
    public ApiResponse<RolePermissionDTO> getById(@PathVariable Long id) {
        return service.getById(id)
                .map(ApiResponse::ok)
                .orElse(ApiResponse.ok(null));
    }

    @DeleteMapping("/{id}")
    public boolean delete(@PathVariable Long id) {
        return service.softDelete(id);
    }
}
