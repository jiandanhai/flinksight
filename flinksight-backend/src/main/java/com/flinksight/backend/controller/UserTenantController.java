package com.flinksight.backend.controller;

import com.flinksight.backend.domain.UserTenant;
import com.flinksight.common.dto.UserTenantDTO;
import com.flinksight.common.service.UserTenantService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Optional;

/**
 * 用户-租户关联管理
 */
@RestController
@RequestMapping("/api/user-tenant")
@RequiredArgsConstructor
public class UserTenantController {

    private final UserTenantService service;

    @PostMapping("/assign")
    public UserTenantDTO assign(@RequestParam Long userId, @RequestParam Long tenantId) {
        return service.assignTenantToUser(userId, tenantId);
    }

    @PostMapping("/remove")
    public boolean remove(@RequestParam Long userId, @RequestParam Long tenantId) {
        return service.removeTenantFromUser(userId, tenantId);
    }

    @GetMapping("/user/{userId}")
    public List<UserTenantDTO> findByUserId(@PathVariable Long userId) {
        return service.findByUserId(userId);
    }

    @GetMapping("/tenant/{tenantId}")
    public List<UserTenantDTO> findByTenantId(@PathVariable Long tenantId) {
        return service.findByTenantId(tenantId);
    }

    @GetMapping("/{id}")
    public Optional<UserTenantDTO> getById(@PathVariable Long id) {
        return service.getById(id);
    }

    @DeleteMapping("/{id}")
    public boolean delete(@PathVariable Long id) {
        return service.softDelete(id);
    }
}
