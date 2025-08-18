package com.flinksight.backend.controller;

import com.flinksight.backend.common.ApiResponse;
import com.flinksight.common.dto.DataSourceDTO;
import com.flinksight.common.model.PageResult;
import com.flinksight.common.service.DataSourceService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

/**
 * 数据源管理
 */
@RestController
@Tag(name = "api", description = "数据资源管理")
@RequestMapping("/api/data-source")
@RequiredArgsConstructor
@Validated
public class DataSourceController {

    private final DataSourceService service;

    @Operation(summary = "", description = "Get clusters by tenant",operationId = "createDataSource")
    @PostMapping
    public ApiResponse<DataSourceDTO> create(@RequestBody  @Valid DataSourceDTO dto) {

        return ApiResponse.ok(service.createOrUpdate(dto));
    }

    @Operation(summary = "", description = "Get clusters by tenant",operationId = "getDataSource")
    @GetMapping("/id/{id}")
    public ApiResponse<DataSourceDTO> getById(@PathVariable Long id) {
        return service.getById(id)
                .map(ApiResponse::ok)
                .orElse(ApiResponse.ok(null));
    }

    @Operation(summary = "", description = "Get clusters by tenant",operationId = "getAllDataSources")
    @GetMapping("/list")
    public ApiResponse<PageResult<DataSourceDTO>> getAll(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size) {
        return ApiResponse.ok(service.getAll(page,size));
    }

    @Operation(summary = "", description = "Get clusters by tenant",operationId = "getDataSourcesByTenant")
    @GetMapping("/tenant/{tenantId}")
    public ApiResponse<PageResult<DataSourceDTO>> findByTenantId(
            @PathVariable Long tenantId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size) {
        return ApiResponse.ok(service.findByTenantId(tenantId,page,size));
    }

    @Operation(summary = "", description = "Get clusters by tenant",operationId = "updateDataSource")
    @PutMapping
    public ApiResponse<DataSourceDTO> update(@RequestBody  @Valid DataSourceDTO dto) {

        return ApiResponse.ok(service.createOrUpdate(dto));
    }

    @Operation(summary = "", description = "Get clusters by tenant",operationId = "deleteDataSource")
    @DeleteMapping("/delete/id/{id}")
    public boolean delete(@PathVariable Long id) {
        return service.softDelete(id);
    }
}
