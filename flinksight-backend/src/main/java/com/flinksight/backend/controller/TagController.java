package com.flinksight.backend.controller;

import com.flinksight.backend.domain.Tag;
import com.flinksight.common.dto.TagDTO;
import com.flinksight.common.service.TagService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Optional;

/**
 * 标签管理
 */
@RestController
@RequestMapping("/api/tag")
@RequiredArgsConstructor
public class TagController {

    private final TagService service;

    @PostMapping
    public TagDTO create(@RequestBody TagDTO dto) {
        return service.createOrUpdate(dto);
    }

    @GetMapping("/{id}")
    public Optional<TagDTO> get(@PathVariable Long id) {
        return service.getById(id);
    }

    @GetMapping
    public List<TagDTO> getAll() {
        return service.getAll();
    }

    @GetMapping("/tenant/{tenantId}")
    public List<TagDTO> findByTenantId(@PathVariable Long tenantId) {
        return service.findByTenantId(tenantId);
    }

    @PutMapping
    public TagDTO update(@RequestBody TagDTO dto) {
        return service.createOrUpdate(dto);
    }

    @DeleteMapping("/{id}")
    public boolean delete(@PathVariable Long id) {
        return service.softDelete(id);
    }
}
