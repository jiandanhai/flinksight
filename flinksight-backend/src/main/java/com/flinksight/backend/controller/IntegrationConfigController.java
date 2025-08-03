package com.flinksight.backend.controller;

import com.flinksight.backend.common.ApiResponse;
import com.flinksight.common.dto.IntegrationConfigDTO;
import com.flinksight.common.model.PageResult;
import com.flinksight.common.service.IntegrationConfigService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

/**
 * 第三方集成配置管理
 */
@RestController
@RequestMapping("/api/integration-config")
@RequiredArgsConstructor
public class IntegrationConfigController {

    private final IntegrationConfigService service;

    @PostMapping
    public ApiResponse<IntegrationConfigDTO> create(@RequestBody IntegrationConfigDTO dto) {
        return ApiResponse.ok(service.createOrUpdate(dto));
    }

    @GetMapping("/{id}")
    public ApiResponse<IntegrationConfigDTO> get(@PathVariable Long id) {
        return service.getById(id)
                .map(ApiResponse::ok)
                .orElse(ApiResponse.ok(null));
    }

    @GetMapping
    public ApiResponse<PageResult<IntegrationConfigDTO>> getAll(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size) {
        return ApiResponse.ok(service.getAll(page,size));
    }

    @GetMapping("/tenant/{tenantId}")
    public ApiResponse<PageResult<IntegrationConfigDTO>> findByTenantId(
            @PathVariable Long tenantId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size) {
        return ApiResponse.ok(service.findByTenantId(tenantId,page,size));
    }

    @PutMapping
    public ApiResponse<IntegrationConfigDTO> update(@RequestBody IntegrationConfigDTO dto) {
        return ApiResponse.ok(service.createOrUpdate(dto));
    }

    @DeleteMapping("/{id}")
    public boolean delete(@PathVariable Long id) {
        return service.softDelete(id);
    }
}
