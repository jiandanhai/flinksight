package com.flinksight.backend.controller;

import com.flinksight.backend.domain.UserPermission;
import com.flinksight.common.dto.UserPermissionDTO;
import com.flinksight.common.service.UserPermissionService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Optional;

/**
 * 用户-权限分配管理
 */
@RestController
@RequestMapping("/api/user-permission")
@RequiredArgsConstructor
public class UserPermissionController {

    private final UserPermissionService service;

    @PostMapping("/assign")
    public UserPermissionDTO assign(@RequestParam Long userId, @RequestParam Long permissionId) {
        return service.assignPermissionToUser(userId, permissionId);
    }

    @PostMapping("/remove")
    public boolean remove(@RequestParam Long userId, @RequestParam Long permissionId) {
        return service.removePermissionFromUser(userId, permissionId);
    }

    @GetMapping("/user/{userId}")
    public List<UserPermissionDTO> findByUser(@PathVariable Long userId) {
        return service.findByUserId(userId);
    }

    @GetMapping("/permission/{permissionId}")
    public List<UserPermissionDTO> findByPermission(@PathVariable Long permissionId) {
        return service.findByPermissionId(permissionId);
    }

    @GetMapping("/{id}")
    public Optional<UserPermissionDTO> getById(@PathVariable Long id) {
        return service.getById(id);
    }

    @DeleteMapping("/{id}")
    public boolean delete(@PathVariable Long id) {
        return service.softDelete(id);
    }
}
