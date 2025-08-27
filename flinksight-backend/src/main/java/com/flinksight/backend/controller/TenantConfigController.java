package com.flinksight.backend.controller;

import com.flinksight.backend.common.ApiResponse;
import com.flinksight.common.dto.TenantConfigDTO;
import com.flinksight.common.model.PageResult;
import com.flinksight.common.service.TenantConfigService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

/**
 * 租户配置管理
 */
@Tag(name = "api", description = "租户配置管理API")
@RestController
@RequestMapping("/api/tenant/config")
@RequiredArgsConstructor
@Validated
public class TenantConfigController {

    private final TenantConfigService service;

    @Operation(summary = "", description = "",operationId = "createTenantConfig")
    @PostMapping
    public ApiResponse<TenantConfigDTO> create(@RequestBody  @Valid TenantConfigDTO dto) {

        return ApiResponse.ok(service.createOrUpdate(dto));
    }

    @Operation(summary = "", description = "",operationId = "getTenantConfig")
    @GetMapping("/id/{id}")
    public Optional<TenantConfigDTO> getById(@PathVariable Long id) {
        return service.getById(id);
    }

    @Operation(summary = "", description = "",operationId = "listTenantConfigs")
    @GetMapping("/list")
    public ApiResponse<PageResult<TenantConfigDTO>> list(@RequestParam Long tenantId,
                                                                  @RequestParam(defaultValue = "0") int page,
                                                                  @RequestParam(defaultValue = "20") int size) {
        return ApiResponse.ok(service.list(tenantId,page,size));
    }

    @Operation(summary = "", description = "",operationId = "updateTenantConfig")
    @PutMapping("/update")
    public ApiResponse<TenantConfigDTO> update(@RequestBody TenantConfigDTO dto) {

        return ApiResponse.ok(service.createOrUpdate(dto));
    }

    @Operation(summary = "", description = "",operationId = "deleteTenantConfig")
    @DeleteMapping("/delete/{id}")
    public boolean delete(@PathVariable Long id) {
        return service.sDelete(id);
    }

    @Operation(summary = "", description = "",operationId = "deleteTenantConfig")
    @DeleteMapping("/delete/batch")
    public boolean deleteBatch(@RequestParam Long tenantId,@RequestBody List<Long> ids) {
        return service.batchSoftDelete(tenantId,ids);
    }
}
