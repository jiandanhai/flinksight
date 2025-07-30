package com.flinksight.backend.controller;

import com.flinksight.backend.domain.MetricDashboard;
import com.flinksight.common.dto.MetricDashboardDTO;
import com.flinksight.common.service.MetricDashboardService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Optional;

/**
 * 指标看板管理
 */
@RestController
@RequestMapping("/api/metric-dashboard")
@RequiredArgsConstructor
public class MetricDashboardController {

    private final MetricDashboardService service;

    @PostMapping
    public MetricDashboardDTO create(@RequestBody MetricDashboardDTO dto) {
        return service.createOrUpdate(dto);
    }

    @GetMapping("/{id}")
    public Optional<MetricDashboardDTO> get(@PathVariable Long id) {
        return service.getById(id);
    }

    @GetMapping
    public List<MetricDashboardDTO> getAll() {
        return service.getAll();
    }

    @GetMapping("/tenant/{tenantId}")
    public List<MetricDashboardDTO> findByTenantId(@PathVariable Long tenantId) {
        return service.findByTenantId(tenantId);
    }

    @PutMapping
    public MetricDashboardDTO update(@RequestBody MetricDashboardDTO dto) {
        return service.createOrUpdate(dto);
    }

    @DeleteMapping("/{id}")
    public boolean delete(@PathVariable Long id) {
        return service.softDelete(id);
    }
}
