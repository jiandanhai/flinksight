package com.flinksight.backend.controller;

import com.flinksight.backend.common.ApiResponse;
import com.flinksight.common.dto.GroupRoleDTO;
import com.flinksight.common.model.PageResult;
import com.flinksight.common.service.GroupRoleService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

/**
 * 组织-角色分配管理
 */
@RestController
@RequestMapping("/api/group-role")
@RequiredArgsConstructor
public class GroupRoleController {

    private final GroupRoleService service;

    @Operation(summary = "", description = "",operationId = "assignGroupRole")
    @PostMapping("/assign")
    public ApiResponse<GroupRoleDTO> assign(@RequestParam Long groupId, @RequestParam Long roleId) {
        return ApiResponse.ok(service.assignRoleToGroup(groupId, roleId));
    }

    @Operation(summary = "", description = "",operationId = "removeGroupRole")
    @PostMapping("/remove")
    public boolean remove(@RequestParam Long groupId, @RequestParam Long roleId) {
        return service.removeRoleFromGroup(groupId, roleId);
    }

    @Operation(summary = "", description = "",operationId = "getGroupRolesByGroup")
    @GetMapping("/group/{groupId}")
    public ApiResponse<PageResult<GroupRoleDTO>> findByGroup(
            @PathVariable Long groupId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size) {
        return ApiResponse.ok(service.findByGroupId(groupId,page,size));
    }

    @Operation(summary = "", description = "",operationId = "getGroupRolesByRole")
    @GetMapping("/role/{roleId}")
    public ApiResponse<PageResult<GroupRoleDTO>> findByRole(
            @PathVariable Long roleId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size) {
        return ApiResponse.ok(service.findByRoleId(roleId,page,size));
    }

    @Operation(summary = "", description = "",operationId = "getGroupRole")
    @GetMapping("/{id}")
    public ApiResponse<GroupRoleDTO> getById(@PathVariable Long id) {
        return service.getById(id)
                .map(ApiResponse::ok)
                .orElse(ApiResponse.ok(null));
    }

    @Operation(summary = "", description = "",operationId = "deleteGroupRole")
    @DeleteMapping("/{id}")
    public boolean delete(@PathVariable Long id) {
        return service.softDelete(id);
    }
}
