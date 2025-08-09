package com.flinksight.backend.controller;

import com.flinksight.backend.common.ApiResponse;
import com.flinksight.common.dto.TenantConfigDTO;
import com.flinksight.common.model.PageResult;
import com.flinksight.common.service.TenantConfigService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

/**
 * 租户配置管理
 */
@RestController
@RequestMapping("/api/tenant-config")
@RequiredArgsConstructor
public class TenantConfigController {

    private final TenantConfigService service;

    @Operation(summary = "", description = "",operationId = "createTenantConfig")
    @PostMapping
    public ApiResponse<TenantConfigDTO> create(@RequestBody TenantConfigDTO dto) {

        return ApiResponse.ok(service.createOrUpdate(dto));
    }

    @Operation(summary = "", description = "",operationId = "getTenantConfig")
    @GetMapping("/{id}")
    public Optional<TenantConfigDTO> getById(@PathVariable Long id) {
        return service.getById(id);
    }

    @Operation(summary = "", description = "",operationId = "getTenantConfigsByTenant")
    @GetMapping("/tenant/{tenantId}")
    public ApiResponse<PageResult<TenantConfigDTO>> findByTenantId(@PathVariable Long tenantId,
                                                                  @RequestParam(defaultValue = "0") int page,
                                                                  @RequestParam(defaultValue = "20") int size) {
        return ApiResponse.ok(service.listByTenantId(tenantId,page,size));
    }

    @Operation(summary = "", description = "",operationId = "updateTenantConfig")
    @PutMapping
    public ApiResponse<TenantConfigDTO> update(@RequestBody TenantConfigDTO dto) {

        return ApiResponse.ok(service.createOrUpdate(dto));
    }

    @Operation(summary = "", description = "",operationId = "deleteTenantConfig")
    @DeleteMapping("/{id}")
    public boolean delete(@PathVariable Long id) {
        return service.softDelete(id);
    }
}
