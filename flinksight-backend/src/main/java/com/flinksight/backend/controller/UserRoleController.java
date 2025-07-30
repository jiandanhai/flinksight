package com.flinksight.backend.controller;

import com.flinksight.backend.domain.UserRole;
import com.flinksight.common.dto.UserRoleDTO;
import com.flinksight.common.service.UserRoleService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Optional;

/**
 * 用户角色分配管理
 */
@RestController
@RequestMapping("/api/user-role")
@RequiredArgsConstructor
public class UserRoleController {

    private final UserRoleService service;

    @PostMapping("/assign")
    public UserRoleDTO assign(@RequestParam Long userId, @RequestParam Long roleId, @RequestParam(required = false) Long tenantId) {
        return service.assignRoleToUser(userId, roleId, tenantId);
    }

    @PostMapping("/remove")
    public boolean remove(@RequestParam Long userId, @RequestParam Long roleId) {
        return service.removeRoleFromUser(userId, roleId);
    }

    @GetMapping("/user/{userId}")
    public List<UserRoleDTO> findRolesByUser(@PathVariable Long userId) {
        return service.findRolesByUserId(userId);
    }

    @GetMapping("/role/{roleId}")
    public List<UserRoleDTO> findUsersByRole(@PathVariable Long roleId) {
        return service.findUsersByRoleId(roleId);
    }

    @GetMapping("/tenant/{tenantId}")
    public List<UserRoleDTO> findByTenantId(@PathVariable Long tenantId) {
        return service.findByTenantId(tenantId);
    }

    @GetMapping("/{id}")
    public Optional<UserRoleDTO> getById(@PathVariable Long id) {
        return service.getById(id);
    }

    @DeleteMapping("/{id}")
    public boolean delete(@PathVariable Long id) {
        return service.softDelete(id);
    }
}
