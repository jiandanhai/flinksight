package com.flinksight.backend.controller;

import com.flinksight.backend.domain.Label;
import com.flinksight.common.dto.LabelDTO;
import com.flinksight.common.service.LabelService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Optional;

/**
 * 标签管理
 */
@RestController
@RequestMapping("/api/label")
@RequiredArgsConstructor
public class LabelController {

    private final LabelService service;

    @PostMapping
    public LabelDTO create(@RequestBody LabelDTO dto) {
        return service.createOrUpdate(dto);
    }

    @GetMapping("/{id}")
    public Optional<LabelDTO> get(@PathVariable Long id) {
        return service.getById(id);
    }

    @GetMapping("/tenant/{tenantId}")
    public List<LabelDTO> findByTenantId(@PathVariable Long tenantId) {
        return service.findByTenantId(tenantId);
    }

    @PutMapping
    public LabelDTO update(@RequestBody LabelDTO dto) {
        return service.createOrUpdate(dto);
    }

    @DeleteMapping("/{id}")
    public boolean delete(@PathVariable Long id) {
        return service.softDelete(id);
    }
}
