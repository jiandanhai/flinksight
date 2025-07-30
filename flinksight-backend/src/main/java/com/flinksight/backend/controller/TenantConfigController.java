package com.flinksight.backend.controller;

import com.flinksight.backend.domain.TenantConfig;
import com.flinksight.common.dto.TenantConfigDTO;
import com.flinksight.common.service.TenantConfigService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import java.util.List;
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
    public TenantConfigDTO create(@RequestBody TenantConfigDTO dto) {
        return service.createOrUpdate(dto);
    }

    @GetMapping("/{id}")
    public Optional<TenantConfigDTO> get(@PathVariable Long id) {
        return service.getById(id);
    }

    @GetMapping("/tenant/{tenantId}")
    public List<TenantConfigDTO> findByTenantId(@PathVariable Long tenantId) {
        return service.listByTenantId(tenantId);
    }

    @PutMapping
    public TenantConfigDTO update(@RequestBody TenantConfigDTO dto) {
        return service.createOrUpdate(dto);
    }

    @DeleteMapping("/{id}")
    public boolean delete(@PathVariable Long id) {
        return service.softDelete(id);
    }
}
