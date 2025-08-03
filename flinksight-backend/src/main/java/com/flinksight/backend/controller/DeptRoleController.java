package com.flinksight.backend.controller;

import com.flinksight.backend.common.ApiResponse;
import com.flinksight.common.dto.DeptRoleDTO;
import com.flinksight.common.model.PageResult;
import com.flinksight.common.service.DeptRoleService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

/**
 * 部门-角色分配管理
 */
@RestController
@RequestMapping("/api/dept-role")
@RequiredArgsConstructor
public class DeptRoleController {

    private final DeptRoleService service;

    @PostMapping("/assign")
    public ApiResponse<DeptRoleDTO> assign(@RequestParam Long deptId, @RequestParam Long roleId) {
        return ApiResponse.ok(service.assignRoleToDept(deptId, roleId));
    }

    @PostMapping("/remove")
    public boolean remove(@RequestParam Long deptId, @RequestParam Long roleId) {
        return service.removeRoleFromDept(deptId, roleId);
    }

    @GetMapping("/dept/{deptId}")
    public ApiResponse<PageResult<DeptRoleDTO>> findByDept(
            @PathVariable Long deptId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size) {
        return ApiResponse.ok(service.findByDeptId(deptId,page,size));
    }

    @GetMapping("/role/{roleId}")
    public ApiResponse<PageResult<DeptRoleDTO>> findByRole(
            @PathVariable Long roleId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size) {
        return ApiResponse.ok(service.findByRoleId(roleId,page,size));
    }

    @GetMapping("/{id}")
    public ApiResponse<DeptRoleDTO> getById(@PathVariable Long id) {
        return service.getById(id)
                .map(ApiResponse::ok)
                .orElse(ApiResponse.ok(null));
    }

    @DeleteMapping("/{id}")
    public boolean delete(@PathVariable Long id) {
        return service.softDelete(id);
    }
}
