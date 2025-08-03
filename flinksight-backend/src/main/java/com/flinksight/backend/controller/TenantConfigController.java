package com.flinksight.backend.controller;

import com.flinksight.backend.common.ApiResponse;
import com.flinksight.common.dto.TenantConfigDTO;
import com.flinksight.common.model.PageResult;
import com.flinksight.common.service.TenantConfigService;
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

    @PostMapping
    public ApiResponse<TenantConfigDTO> create(@RequestBody TenantConfigDTO dto) {

        return ApiResponse.ok(service.createOrUpdate(dto));
    }

    @GetMapping("/{id}")
    public Optional<TenantConfigDTO> get(@PathVariable Long id) {
        return service.getById(id);
    }

    @GetMapping("/tenant/{tenantId}")
    public ApiResponse<PageResult<TenantConfigDTO>> findByTenantId(@PathVariable Long tenantId,
                                                                  @RequestParam(defaultValue = "0") int page,
                                                                  @RequestParam(defaultValue = "20") int size) {
        return ApiResponse.ok(service.listByTenantId(tenantId,page,size));
    }

    @PutMapping
    public ApiResponse<TenantConfigDTO> update(@RequestBody TenantConfigDTO dto) {

        return ApiResponse.ok(service.createOrUpdate(dto));
    }

    @DeleteMapping("/{id}")
    public boolean delete(@PathVariable Long id) {
        return service.softDelete(id);
    }
}
