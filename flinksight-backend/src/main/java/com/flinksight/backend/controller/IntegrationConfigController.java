package com.flinksight.backend.controller;

import com.flinksight.backend.common.ApiResponse;
import com.flinksight.common.dto.IntegrationConfigDTO;
import com.flinksight.common.model.PageResult;
import com.flinksight.common.service.IntegrationConfigService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

/**
 * 第三方集成配置管理
 */
@RestController
@Tag(name = "api", description = "第三方集成配置管理")
@RequestMapping("/api/integration-config")
@RequiredArgsConstructor
public class IntegrationConfigController {

    private final IntegrationConfigService service;

    @Operation(summary = "", description = "",operationId = "createIntegrationConfig")
    @PostMapping
    public ApiResponse<IntegrationConfigDTO> create(@RequestBody IntegrationConfigDTO dto) {
        return ApiResponse.ok(service.createOrUpdate(dto));
    }

    @Operation(summary = "", description = "",operationId = "getIntegrationConfig")
    @GetMapping("/{id}")
    public ApiResponse<IntegrationConfigDTO> getById(@PathVariable Long id) {
        return service.getById(id)
                .map(ApiResponse::ok)
                .orElse(ApiResponse.ok(null));
    }

    @Operation(summary = "", description = "",operationId = "getAllIntegrationConfigs")
    @GetMapping
    public ApiResponse<PageResult<IntegrationConfigDTO>> getAll(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size) {
        return ApiResponse.ok(service.getAll(page,size));
    }

    @Operation(summary = "", description = "",operationId = "getIntegrationConfigsByTenant")
    @GetMapping("/tenant/{tenantId}")
    public ApiResponse<PageResult<IntegrationConfigDTO>> findByTenantId(
            @PathVariable Long tenantId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size) {
        return ApiResponse.ok(service.findByTenantId(tenantId,page,size));
    }

    @Operation(summary = "", description = "",operationId = "updateIntegrationConfig")
    @PutMapping
    public ApiResponse<IntegrationConfigDTO> update(@RequestBody IntegrationConfigDTO dto) {
        return ApiResponse.ok(service.createOrUpdate(dto));
    }

    @Operation(summary = "", description = "",operationId = "deleteIntegrationConfig")
    @DeleteMapping("/{id}")
    public boolean delete(@PathVariable Long id) {
        return service.softDelete(id);
    }
}
