package com.flinksight.backend.controller;

import com.flinksight.backend.domain.Resource;
import com.flinksight.common.dto.ResourceDTO;
import com.flinksight.common.service.ResourceService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Optional;

/**
 * 资源管理
 */
@RestController
@RequestMapping("/api/resource")
@RequiredArgsConstructor
public class ResourceController {

    private final ResourceService service;

    @PostMapping
    public ResourceDTO create(@RequestBody ResourceDTO dto) {
        return service.createOrUpdate(dto);
    }

    @GetMapping("/{id}")
    public Optional<ResourceDTO> get(@PathVariable Long id) {
        return service.getById(id);
    }

    @GetMapping
    public List<ResourceDTO> getAll() {
        return service.getAll();
    }

    @GetMapping("/tenant/{tenantId}")
    public List<ResourceDTO> findByTenantId(@PathVariable Long tenantId) {
        return service.findByTenantId(tenantId);
    }

    @PutMapping
    public ResourceDTO update(@RequestBody ResourceDTO dto) {
        return service.createOrUpdate(dto);
    }

    @DeleteMapping("/{id}")
    public boolean delete(@PathVariable Long id) {
        return service.softDelete(id);
    }
}
