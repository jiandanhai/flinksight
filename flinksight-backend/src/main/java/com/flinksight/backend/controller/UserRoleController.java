package com.flinksight.backend.controller;

import com.flinksight.backend.common.ApiResponse;
import com.flinksight.common.dto.UserRoleDTO;
import com.flinksight.common.model.PageResult;
import com.flinksight.common.service.UserRoleService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

/**
 * 用户角色分配管理
 */
@RestController
@RequestMapping("/api/user-role")
@RequiredArgsConstructor
public class UserRoleController {

    private final UserRoleService service;

    @PostMapping("/assign")
    public ApiResponse<UserRoleDTO> assign(@RequestParam Long userId, @RequestParam Long roleId, @RequestParam(required = false) Long tenantId) {
        return ApiResponse.ok(service.assignRoleToUser(userId, roleId, tenantId));
    }

    @PostMapping("/remove")
    public boolean remove(@RequestParam Long userId, @RequestParam Long roleId) {
        return service.removeRoleFromUser(userId, roleId);
    }

    @GetMapping("/user/{userId}")
    public ApiResponse<PageResult<UserRoleDTO>> findRolesByUser(@PathVariable Long userId,
                                                               @RequestParam(defaultValue = "0") int page,
                                                               @RequestParam(defaultValue = "20") int size) {

        return ApiResponse.ok(service.findRolesByUserId(userId,page,size));
    }

    @GetMapping("/role/{roleId}")
    public ApiResponse<PageResult<UserRoleDTO>> findUsersByRole(@PathVariable Long roleId,
                                             @RequestParam(defaultValue = "0") int page,
                                             @RequestParam(defaultValue = "20") int size) {
        return ApiResponse.ok(service.findUsersByRoleId(roleId,page,size));
    }

    @GetMapping("/tenant/{tenantId}")
    public ApiResponse<PageResult<UserRoleDTO>> findByTenantId(@PathVariable Long tenantId,
                                            @RequestParam(defaultValue = "0") int page,
                                            @RequestParam(defaultValue = "20") int size) {
        return ApiResponse.ok(service.findByTenantId(tenantId,page,size));
    }

    @GetMapping("/{id}")
    public ApiResponse<UserRoleDTO> getById(@PathVariable Long id) {
        return service.getById(id)
                .map(ApiResponse::ok)
                .orElse(ApiResponse.ok(null));
    }

    @DeleteMapping("/{id}")
    public boolean delete(@PathVariable Long id) {
        return service.softDelete(id);
    }
}
