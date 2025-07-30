package com.flinksight.backend.controller;

import com.flinksight.backend.domain.File;
import com.flinksight.common.dto.FileDTO;
import com.flinksight.common.service.FileService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Optional;

/**
 * 文件管理
 */
@RestController
@RequestMapping("/api/file")
@RequiredArgsConstructor
public class FileController {

    private final FileService service;

    @PostMapping
    public FileDTO create(@RequestBody FileDTO dto) {
        return service.createOrUpdate(dto);
    }

    @GetMapping("/{id}")
    public Optional<FileDTO> get(@PathVariable Long id) {
        return service.getById(id);
    }

    @GetMapping
    public List<FileDTO> getAll() {
        return service.getAll();
    }

    @GetMapping("/tenant/{tenantId}")
    public List<FileDTO> findByTenantId(@PathVariable Long tenantId) {
        return service.findByTenantId(tenantId);
    }

    @PutMapping
    public FileDTO update(@RequestBody FileDTO dto) {
        return service.createOrUpdate(dto);
    }

    @DeleteMapping("/{id}")
    public boolean delete(@PathVariable Long id) {
        return service.softDelete(id);
    }
}
