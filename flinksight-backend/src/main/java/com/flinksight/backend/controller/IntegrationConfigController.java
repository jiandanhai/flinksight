package com.flinksight.backend.controller;

import com.flinksight.backend.domain.IntegrationConfig;
import com.flinksight.common.dto.IntegrationConfigDTO;
import com.flinksight.common.service.IntegrationConfigService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Optional;

/**
 * 第三方集成配置管理
 */
@RestController
@RequestMapping("/api/integration-config")
@RequiredArgsConstructor
public class IntegrationConfigController {

    private final IntegrationConfigService service;

    @PostMapping
    public IntegrationConfigDTO create(@RequestBody IntegrationConfigDTO dto) {
        return service.createOrUpdate(dto);
    }

    @GetMapping("/{id}")
    public Optional<IntegrationConfigDTO> get(@PathVariable Long id) {
        return service.getById(id);
    }

    @GetMapping
    public List<IntegrationConfigDTO> getAll() {
        return service.getAll();
    }

    @GetMapping("/tenant/{tenantId}")
    public List<IntegrationConfigDTO> findByTenantId(@PathVariable Long tenantId) {
        return service.findByTenantId(tenantId);
    }

    @PutMapping
    public IntegrationConfigDTO update(@RequestBody IntegrationConfigDTO dto) {
        return service.createOrUpdate(dto);
    }

    @DeleteMapping("/{id}")
    public boolean delete(@PathVariable Long id) {
        return service.softDelete(id);
    }
}
