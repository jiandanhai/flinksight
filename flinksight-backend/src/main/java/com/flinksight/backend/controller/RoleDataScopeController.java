package com.flinksight.backend.controller;

import com.flinksight.backend.common.ApiResponse;
import com.flinksight.common.dto.RoleDataScopeDTO;
import com.flinksight.common.model.PageResult;
import com.flinksight.common.service.RoleDataScopeService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

/**
 * 角色-数据权限分配管理
 */
@RestController
@RequestMapping("/api/role-data-scope")
@RequiredArgsConstructor
public class RoleDataScopeController {

    private final RoleDataScopeService service;

    @PostMapping("/assign")
    public ApiResponse<RoleDataScopeDTO> assign(@RequestParam Long roleId, @RequestParam Long dataScopeId) {
        return ApiResponse.ok(service.assignDataScopeToRole(roleId, dataScopeId));
    }

    @PostMapping("/remove")
    public boolean remove(@RequestParam Long roleId, @RequestParam Long dataScopeId) {
        return service.removeDataScopeFromRole(roleId, dataScopeId);
    }

    @GetMapping("/role/{roleId}")
    public ApiResponse<PageResult<RoleDataScopeDTO>> findByRole(
            @PathVariable Long roleId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size) {
        return ApiResponse.ok(service.findByRoleId(roleId,page,size));
    }

    @GetMapping("/data-scope/{dataScopeId}")
    public ApiResponse<PageResult<RoleDataScopeDTO>> findByDataScope(@PathVariable Long dataScopeId,
                                                                    @RequestParam(defaultValue = "0") int page,
                                                                    @RequestParam(defaultValue = "20") int size) {
        return ApiResponse.ok(service.findByDataScopeId(dataScopeId,page,size));
    }

    @GetMapping("/{id}")
    public ApiResponse<RoleDataScopeDTO> getById(@PathVariable Long id) {
        return service.getById(id)
                .map(ApiResponse::ok)
                .orElse(ApiResponse.ok(null));
    }

    @DeleteMapping("/{id}")
    public boolean delete(@PathVariable Long id) {
        return service.softDelete(id);
    }
}
