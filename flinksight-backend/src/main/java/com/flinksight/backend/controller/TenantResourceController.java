package com.flinksight.backend.controller;

import com.flinksight.backend.common.ApiResponse;
import com.flinksight.common.dto.TenantResourceDTO;
import com.flinksight.common.model.PageResult;
import com.flinksight.common.service.TenantResourceService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

/**
 * 租户-资源分配管理
 */
@Tag(name = "api", description = "租户-资源分配管理API")
@RestController
@RequestMapping("/api/tenant-resource")
@RequiredArgsConstructor
@Validated
public class TenantResourceController {

    private final TenantResourceService service;

    @Operation(summary = "创建", description = "Delete tenant",operationId = "assignTenantResource")
    @PostMapping("/assign")
    public ApiResponse<TenantResourceDTO> assign(@RequestParam Long tenantId, @RequestParam Long resourceId) {
        return ApiResponse.ok(service.assignResourceToTenant(tenantId, resourceId));
    }

    @Operation(summary = "删除租户", description = "Delete tenant",operationId = "removeTenantResource")
    @PostMapping("/remove")
    public boolean remove(@RequestParam Long tenantId, @RequestParam Long resourceId) {
        return service.removeResourceFromTenant(tenantId, resourceId);
    }

    @Operation(summary = "删除租户", description = "Delete tenant",operationId = "getTenantResourcesByTenant")
    @GetMapping("/tenant/{tenantId}")
    public ApiResponse<PageResult<TenantResourceDTO>> findByTenant(
            @PathVariable Long tenantId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size) {
        return ApiResponse.ok(service.findByTenantId(tenantId,page,size));
    }

    @Operation(summary = "删除租户", description = "Delete tenant",operationId = "getTenantResourcesByResource")
    @GetMapping("/resource/{resourceId}")
    public ApiResponse<PageResult<TenantResourceDTO>> findByResource(
            @PathVariable Long resourceId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size) {
        return ApiResponse.ok(service.findByResourceId(resourceId,page,size));
    }

    @Operation(summary = "删除租户", description = "Delete tenant",operationId = "getTenantResource")
    @GetMapping("/id/{id}")
    public ApiResponse<TenantResourceDTO> getById(@PathVariable Long id) {
        return service.getById(id)
                .map(ApiResponse::ok)
                .orElse(ApiResponse.ok(null));
    }

    @Operation(summary = "删除租户", description = "Delete tenant",operationId = "deleteTenantResource")
    @DeleteMapping("/delete/{id}")
    public boolean delete(@PathVariable Long id) {
        return service.softDelete(id);
    }
}
