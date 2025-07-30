package com.flinksight.backend.controller;

import com.flinksight.backend.domain.ResourceGroup;
import com.flinksight.common.dto.ResourceGroupDTO;
import com.flinksight.common.service.ResourceGroupService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Optional;

/**
 * 资源分组管理
 */
@RestController
@RequestMapping("/api/resource-group")
@RequiredArgsConstructor
public class ResourceGroupController {

    private final ResourceGroupService service;

    @PostMapping
    public ResourceGroupDTO create(@RequestBody ResourceGroupDTO dto) {
        return service.createOrUpdate(dto);
    }

    @GetMapping("/{id}")
    public Optional<ResourceGroupDTO> get(@PathVariable Long id) {
        return service.getById(id);
    }

    @GetMapping
    public List<ResourceGroupDTO> getAll() {
        return service.getAll();
    }

    @GetMapping("/tenant/{tenantId}")
    public List<ResourceGroupDTO> findByTenantId(@PathVariable Long tenantId) {
        return service.findByTenantId(tenantId);
    }

    @PutMapping
    public ResourceGroupDTO update(@RequestBody ResourceGroupDTO dto) {
        return service.createOrUpdate(dto);
    }

    @DeleteMapping("/{id}")
    public boolean delete(@PathVariable Long id) {
        return service.softDelete(id);
    }
}
