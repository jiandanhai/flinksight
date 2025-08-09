package com.flinksight.backend.controller;

import com.flinksight.backend.common.ApiResponse;
import com.flinksight.common.dto.MetricDashboardDTO;
import com.flinksight.common.model.PageResult;
import com.flinksight.common.service.MetricDashboardService;
import io.swagger.v3.oas.annotations.Operation;
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

    @Operation(summary = "", description = "",operationId = "createMetricDashboard")
    @PostMapping
    public MetricDashboardDTO create(@RequestBody MetricDashboardDTO dto) {
        return service.createOrUpdate(dto);
    }

    @Operation(summary = "", description = "",operationId = "getMetricDashboard")
    @GetMapping("/{id}")
    public Optional<MetricDashboardDTO> getById(@PathVariable Long id) {
        return service.getById(id);
    }

    @Operation(summary = "", description = "",operationId = "getAllMetricDashboards")
    @GetMapping
    public ApiResponse<PageResult<MetricDashboardDTO>> getAll(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size) {
        return ApiResponse.ok(service.getAll(page,size));
    }

    @Operation(summary = "", description = "",operationId = "getMetricDashboardsByTenant")
    @GetMapping("/tenant/{tenantId}")
    public ApiResponse<PageResult<MetricDashboardDTO>> findByTenantId(
            @PathVariable Long tenantId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size) {
        return ApiResponse.ok(service.findByTenantId(tenantId,page,size));
    }

    @Operation(summary = "", description = "",operationId = "updateMetricDashboard")
    @PutMapping
    public ApiResponse<MetricDashboardDTO> update(@RequestBody MetricDashboardDTO dto) {
        return ApiResponse.ok(service.createOrUpdate(dto));
    }

    @Operation(summary = "", description = "",operationId = "deleteMetricDashboard")
    @DeleteMapping("/{id}")
    public boolean delete(@PathVariable Long id) {
        return service.softDelete(id);
    }
}
