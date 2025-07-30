package com.flinksight.backend.controller;

import com.flinksight.backend.domain.OperationTemplate;
import com.flinksight.common.dto.OperationTemplateDTO;
import com.flinksight.common.service.OperationTemplateService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Optional;

/**
 * 操作模板管理
 */
@RestController
@RequestMapping("/api/operation-template")
@RequiredArgsConstructor
public class OperationTemplateController {

    private final OperationTemplateService service;

    @PostMapping
    public OperationTemplateDTO create(@RequestBody OperationTemplateDTO dto) {
        return service.createOrUpdate(dto);
    }

    @GetMapping("/{id}")
    public Optional<OperationTemplateDTO> get(@PathVariable Long id) {
        return service.getById(id);
    }

    @GetMapping
    public List<OperationTemplateDTO> getAll() {
        return service.getAll();
    }

    @GetMapping("/tenant/{tenantId}")
    public List<OperationTemplateDTO> findByTenantId(@PathVariable Long tenantId) {
        return service.findByTenantId(tenantId);
    }

    @PutMapping
    public OperationTemplateDTO update(@RequestBody OperationTemplateDTO dto) {
        return service.createOrUpdate(dto);
    }

    @DeleteMapping("/{id}")
    public boolean delete(@PathVariable Long id) {
        return service.softDelete(id);
    }
}
