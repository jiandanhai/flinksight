package com.flinksight.backend.controller;

import com.flinksight.backend.common.ApiResponse;
import com.flinksight.common.dto.UserPermissionDTO;
import com.flinksight.common.model.PageResult;
import com.flinksight.common.service.UserPermissionService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

/**
 * 用户-权限分配管理
 */
@RestController
@RequestMapping("/api/user-permission")
@RequiredArgsConstructor
public class UserPermissionController {

    private final UserPermissionService service;

    @PostMapping("/assign")
    public ApiResponse<UserPermissionDTO> assign(@RequestParam Long userId, @RequestParam Long permissionId) {
        return ApiResponse.ok(service.assignPermissionToUser(userId, permissionId));
    }

    @PostMapping("/remove")
    public boolean remove(@RequestParam Long userId, @RequestParam Long permissionId) {
        return service.removePermissionFromUser(userId, permissionId);
    }

    @GetMapping("/user/{userId}")
    public ApiResponse<PageResult<UserPermissionDTO>> findByUser(@PathVariable Long userId,
                                              @RequestParam(defaultValue = "0") int page,
                                              @RequestParam(defaultValue = "20") int size) {
        return ApiResponse.ok(service.findByUserId(userId,page,size));
    }

    @GetMapping("/permission/{permissionId}")
    public ApiResponse<PageResult<UserPermissionDTO>> findByPermission(@PathVariable Long permissionId,
                                                                      @RequestParam(defaultValue = "0") int page,
                                                                      @RequestParam(defaultValue = "20") int size) {
        return ApiResponse.ok(service.findByPermissionId(permissionId,page,size));
    }

    @GetMapping("/{id}")
    public ApiResponse<UserPermissionDTO> getById(@PathVariable Long id) {
        return service.getById(id)
                .map(ApiResponse::ok)
                .orElse(ApiResponse.ok(null));
    }

    @DeleteMapping("/{id}")
    public boolean delete(@PathVariable Long id) {
        return service.softDelete(id);
    }
}
