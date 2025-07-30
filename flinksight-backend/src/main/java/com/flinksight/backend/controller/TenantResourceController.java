package com.flinksight.backend.controller;

import com.flinksight.backend.domain.TenantResource;
import com.flinksight.common.dto.TenantResourceDTO;
import com.flinksight.common.service.TenantResourceService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Optional;

/**
 * 租户-资源分配管理
 */
@RestController
@RequestMapping("/api/tenant-resource")
@RequiredArgsConstructor
public class TenantResourceController {

    private final TenantResourceService service;

    @PostMapping("/assign")
    public TenantResourceDTO assign(@RequestParam Long tenantId, @RequestParam Long resourceId) {
        return service.assignResourceToTenant(tenantId, resourceId);
    }

    @PostMapping("/remove")
    public boolean remove(@RequestParam Long tenantId, @RequestParam Long resourceId) {
        return service.removeResourceFromTenant(tenantId, resourceId);
    }

    @GetMapping("/tenant/{tenantId}")
    public List<TenantResourceDTO> findByTenant(@PathVariable Long tenantId) {
        return service.findByTenantId(tenantId);
    }

    @GetMapping("/resource/{resourceId}")
    public List<TenantResourceDTO> findByResource(@PathVariable Long resourceId) {
        return service.findByResourceId(resourceId);
    }

    @GetMapping("/{id}")
    public Optional<TenantResourceDTO> getById(@PathVariable Long id) {
        return service.getById(id);
    }

    @DeleteMapping("/{id}")
    public boolean delete(@PathVariable Long id) {
        return service.softDelete(id);
    }
}
