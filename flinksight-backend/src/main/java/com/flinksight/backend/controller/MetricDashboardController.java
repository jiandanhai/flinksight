package com.flinksight.backend.controller;

import com.flinksight.backend.common.ApiResponse;
import com.flinksight.common.dto.MetricDashboardDTO;
import com.flinksight.common.model.PageResult;
import com.flinksight.common.service.MetricDashboardService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

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
    public ApiResponse<PageResult<MetricDashboardDTO>> getAll(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size) {
        return ApiResponse.ok(service.getAll(page,size));
    }

    @GetMapping("/tenant/{tenantId}")
    public ApiResponse<PageResult<MetricDashboardDTO>> findByTenantId(
            @PathVariable Long tenantId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size) {
        return ApiResponse.ok(service.findByTenantId(tenantId,page,size));
    }

    @PutMapping
    public ApiResponse<MetricDashboardDTO> update(@RequestBody MetricDashboardDTO dto) {
        return ApiResponse.ok(service.createOrUpdate(dto));
    }

    @DeleteMapping("/{id}")
    public boolean delete(@PathVariable Long id) {
        return service.softDelete(id);
    }
}
