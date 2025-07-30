package com.flinksight.backend.controller;

import com.flinksight.backend.domain.DeptRole;
import com.flinksight.common.dto.DeptRoleDTO;
import com.flinksight.common.service.DeptRoleService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Optional;

/**
 * 部门-角色分配管理
 */
@RestController
@RequestMapping("/api/dept-role")
@RequiredArgsConstructor
public class DeptRoleController {

    private final DeptRoleService service;

    @PostMapping("/assign")
    public DeptRoleDTO assign(@RequestParam Long deptId, @RequestParam Long roleId) {
        return service.assignRoleToDept(deptId, roleId);
    }

    @PostMapping("/remove")
    public boolean remove(@RequestParam Long deptId, @RequestParam Long roleId) {
        return service.removeRoleFromDept(deptId, roleId);
    }

    @GetMapping("/dept/{deptId}")
    public List<DeptRoleDTO> findByDept(@PathVariable Long deptId) {
        return service.findByDeptId(deptId);
    }

    @GetMapping("/role/{roleId}")
    public List<DeptRoleDTO> findByRole(@PathVariable Long roleId) {
        return service.findByRoleId(roleId);
    }

    @GetMapping("/{id}")
    public Optional<DeptRoleDTO> getById(@PathVariable Long id) {
        return service.getById(id);
    }

    @DeleteMapping("/{id}")
    public boolean delete(@PathVariable Long id) {
        return service.softDelete(id);
    }
}
