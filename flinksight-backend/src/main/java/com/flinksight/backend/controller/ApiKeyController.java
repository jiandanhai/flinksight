package com.flinksight.backend.controller;

import com.flinksight.backend.domain.ApiKey;
import com.flinksight.common.dto.ApiKeyDTO;
import com.flinksight.common.service.ApiKeyService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

/**
 * API密钥管理
 */
@RestController
@RequestMapping("/api/api-key")
@RequiredArgsConstructor
public class ApiKeyController {

    private final ApiKeyService service;

    @PostMapping
    public ApiKeyDTO create(@RequestBody ApiKeyDTO dto) {
        return service.createOrUpdate(dto);
    }

    @GetMapping("/{id}")
    public Optional<ApiKeyDTO> get(@PathVariable Long id) {
        return service.getById(id);
    }

    @GetMapping
    public List<ApiKeyDTO> getAll() {
        return service.getAll();
    }

    @GetMapping("/tenant/{tenantId}")
    public List<ApiKeyDTO> findByTenantId(@PathVariable Long tenantId) {
        return service.findByTenantId(tenantId);
    }

    @PutMapping
    public ApiKeyDTO update(@RequestBody ApiKeyDTO dto) {
        return service.createOrUpdate(dto);
    }

    @DeleteMapping("/{id}")
    public boolean delete(@PathVariable Long id) {
        return service.softDelete(id);
    }
}
