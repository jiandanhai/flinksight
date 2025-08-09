package com.flinksight.backend.controller;

import com.flinksight.backend.common.ApiResponse;
import com.flinksight.common.dto.UserPermissionDTO;
import com.flinksight.common.model.PageResult;
import com.flinksight.common.service.UserPermissionService;
import io.swagger.v3.oas.annotations.Operation;
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

    @Operation(summary = "", description = "",operationId = "assignUserPermission")
    @PostMapping("/assign")
    public ApiResponse<UserPermissionDTO> assign(@RequestParam Long userId, @RequestParam Long permissionId) {
        return ApiResponse.ok(service.assignPermissionToUser(userId, permissionId));
    }

    @Operation(summary = "", description = "",operationId = "removeUserPermission")
    @PostMapping("/remove")
    public boolean remove(@RequestParam Long userId, @RequestParam Long permissionId) {
        return service.removePermissionFromUser(userId, permissionId);
    }

    @Operation(summary = "", description = "",operationId = "getUserPermissionsByUser")
    @GetMapping("/user/{userId}")
    public ApiResponse<PageResult<UserPermissionDTO>> findByUser(@PathVariable Long userId,
                                              @RequestParam(defaultValue = "0") int page,
                                              @RequestParam(defaultValue = "20") int size) {
        return ApiResponse.ok(service.findByUserId(userId,page,size));
    }

    @Operation(summary = "", description = "",operationId = "getUserPermissionsByPermission")
    @GetMapping("/permission/{permissionId}")
    public ApiResponse<PageResult<UserPermissionDTO>> findByPermission(@PathVariable Long permissionId,
                                                                      @RequestParam(defaultValue = "0") int page,
                                                                      @RequestParam(defaultValue = "20") int size) {
        return ApiResponse.ok(service.findByPermissionId(permissionId,page,size));
    }

    @Operation(summary = "", description = "",operationId = "getUserPermission")
    @GetMapping("/{id}")
    public ApiResponse<UserPermissionDTO> getById(@PathVariable Long id) {
        return service.getById(id)
                .map(ApiResponse::ok)
                .orElse(ApiResponse.ok(null));
    }

    @Operation(summary = "", description = "",operationId = "deleteUserPermission")
    @DeleteMapping("/{id}")
    public boolean delete(@PathVariable Long id) {
        return service.softDelete(id);
    }
}
