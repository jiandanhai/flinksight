package com.flinksight.backend.controller;

import com.flinksight.backend.domain.RolePermission;
import com.flinksight.common.dto.RolePermissionDTO;
import com.flinksight.common.service.RolePermissionService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Optional;

/**
 * 角色权限分配管理
 */
@RestController
@RequestMapping("/api/role-permission")
@RequiredArgsConstructor
public class RolePermissionController {

    private final RolePermissionService service;

    @PostMapping("/assign")
    public RolePermissionDTO assign(@RequestParam Long roleId, @RequestParam Long permissionId) {
        return service.assignPermissionToRole(roleId, permissionId);
    }

    @PostMapping("/remove")
    public boolean remove(@RequestParam Long roleId, @RequestParam Long permissionId) {
        return service.removePermissionFromRole(roleId, permissionId);
    }

    @GetMapping("/role/{roleId}")
    public List<RolePermissionDTO> findByRoleId(@PathVariable Long roleId) {
        return service.findByRoleId(roleId);
    }

    @GetMapping("/permission/{permissionId}")
    public List<RolePermissionDTO> findByPermissionId(@PathVariable Long permissionId) {
        return service.findByPermissionId(permissionId);
    }

    @GetMapping("/{id}")
    public Optional<RolePermissionDTO> getById(@PathVariable Long id) {
        return service.getById(id);
    }

    @DeleteMapping("/{id}")
    public boolean delete(@PathVariable Long id) {
        return service.softDelete(id);
    }
}
