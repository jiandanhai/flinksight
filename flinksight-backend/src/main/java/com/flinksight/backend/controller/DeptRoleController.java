package com.flinksight.backend.controller;

import com.flinksight.backend.common.ApiResponse;
import com.flinksight.common.dto.DeptRoleDTO;
import com.flinksight.common.model.PageResult;
import com.flinksight.common.service.DeptRoleService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

/**
 * 部门-角色分配管理
 */
@RestController
@Tag(name = "api", description = "部门-角色分配管理")
@RequestMapping("/api/dept-role")
@RequiredArgsConstructor
@Validated
public class DeptRoleController {

    private final DeptRoleService service;

    @Operation(summary = "", description = "",operationId = "assignDeptRole")
    @PostMapping("/assign")
    public ApiResponse<DeptRoleDTO> assign(@RequestParam Long deptId, @RequestParam Long roleId) {
        return ApiResponse.ok(service.assignRoleToDept(deptId, roleId));
    }

    @Operation(summary = "", description = "",operationId = "removeDeptRole")
    @PostMapping("/remove")
    public boolean remove(@RequestParam Long deptId, @RequestParam Long roleId) {
        return service.removeRoleFromDept(deptId, roleId);
    }

    @Operation(summary = "", description = "",operationId = "getDeptRolesByDept")
    @GetMapping("/dept/{deptId}")
    public ApiResponse<PageResult<DeptRoleDTO>> findByDept(
            @PathVariable Long deptId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size) {
        return ApiResponse.ok(service.findByDeptId(deptId,page,size));
    }

    @Operation(summary = "", description = "",operationId = "getDeptRolesByRole")
    @GetMapping("/role/{roleId}")
    public ApiResponse<PageResult<DeptRoleDTO>> findByRole(
            @PathVariable Long roleId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size) {
        return ApiResponse.ok(service.findByRoleId(roleId,page,size));
    }

    @Operation(summary = "", description = "",operationId = "getDeptRole")
    @GetMapping("/{id}")
    public ApiResponse<DeptRoleDTO> getById(@PathVariable Long id) {
        return service.getById(id)
                .map(ApiResponse::ok)
                .orElse(ApiResponse.ok(null));
    }

    @Operation(summary = "", description = "",operationId = "deleteDeptRole")
    @DeleteMapping("/{id}")
    public boolean delete(@PathVariable Long id) {
        return service.softDelete(id);
    }
}
