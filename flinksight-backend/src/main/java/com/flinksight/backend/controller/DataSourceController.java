package com.flinksight.backend.controller;

import com.flinksight.backend.domain.DataSource;
import com.flinksight.common.dto.DataSourceDTO;
import com.flinksight.common.service.DataSourceService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Optional;

/**
 * 数据源管理
 */
@RestController
@RequestMapping("/api/data-source")
@RequiredArgsConstructor
public class DataSourceController {

    private final DataSourceService service;

    @PostMapping
    public DataSourceDTO create(@RequestBody DataSourceDTO dto) {
        return service.createOrUpdate(dto);
    }

    @GetMapping("/{id}")
    public Optional<DataSourceDTO> get(@PathVariable Long id) {
        return service.getById(id);
    }

    @GetMapping
    public List<DataSourceDTO> getAll() {
        return service.getAll();
    }

    @GetMapping("/tenant/{tenantId}")
    public List<DataSourceDTO> findByTenantId(@PathVariable Long tenantId) {
        return service.findByTenantId(tenantId);
    }

    @PutMapping
    public DataSourceDTO update(@RequestBody DataSourceDTO dto) {
        return service.createOrUpdate(dto);
    }

    @DeleteMapping("/{id}")
    public boolean delete(@PathVariable Long id) {
        return service.softDelete(id);
    }
}
