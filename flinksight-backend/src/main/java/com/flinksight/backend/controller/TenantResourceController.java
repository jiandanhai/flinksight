package com.flinksight.backend.controller;

import com.flinksight.backend.common.ApiResponse;
import com.flinksight.common.dto.TenantResourceDTO;
import com.flinksight.common.model.PageResult;
import com.flinksight.common.service.TenantResourceService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

/**
 * 租户-资源分配管理
 */
@RestController
@RequestMapping("/api/tenant-resource")
@RequiredArgsConstructor
public class TenantResourceController {

    private final TenantResourceService service;

    @PostMapping("/assign")
    public ApiResponse<TenantResourceDTO> assign(@RequestParam Long tenantId, @RequestParam Long resourceId) {
        return ApiResponse.ok(service.assignResourceToTenant(tenantId, resourceId));
    }

    @PostMapping("/remove")
    public boolean remove(@RequestParam Long tenantId, @RequestParam Long resourceId) {
        return service.removeResourceFromTenant(tenantId, resourceId);
    }

    @GetMapping("/tenant/{tenantId}")
    public ApiResponse<PageResult<TenantResourceDTO>> findByTenant(
            @PathVariable Long tenantId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size) {
        return ApiResponse.ok(service.findByTenantId(tenantId,page,size));
    }

    @GetMapping("/resource/{resourceId}")
    public ApiResponse<PageResult<TenantResourceDTO>> findByResource(
            @PathVariable Long resourceId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size) {
        return ApiResponse.ok(service.findByResourceId(resourceId,page,size));
    }

    @GetMapping("/{id}")
    public ApiResponse<TenantResourceDTO> getById(@PathVariable Long id) {
        return service.getById(id)
                .map(ApiResponse::ok)
                .orElse(ApiResponse.ok(null));
    }

    @DeleteMapping("/{id}")
    public boolean delete(@PathVariable Long id) {
        return service.softDelete(id);
    }
}
