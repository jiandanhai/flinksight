package com.flinksight.backend.controller;

import com.flinksight.backend.common.ApiResponse;
import com.flinksight.backend.security.tenant.TenantRequired;
import com.flinksight.common.dto.TenantDTO;
import com.flinksight.common.model.PageResult;
import com.flinksight.common.service.TenantService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

/**
 * 租户管理接口
 */
@Tag(name = "api", description = "租户管理接口API")
@RestController
@RequestMapping("/api/tenant")
@RequiredArgsConstructor
@TenantRequired
@Validated
public class TenantController {

    private final TenantService tenantService;

    @Operation(summary = "新建租户", description = "Create new tenant",operationId = "createTenant")
    @PostMapping("/create")
    public ApiResponse<TenantDTO> createTenant(@RequestBody  @Valid TenantDTO dto) {
        return ApiResponse.ok(tenantService.createTenant(dto));
    }

    @Operation(summary = "根据ID查询租户", description = "Get tenant by ID",operationId = "getTenant")
    @GetMapping("/id/{id}")
    public ApiResponse<TenantDTO> getById(@PathVariable Long id) {
        return tenantService.getTenantById(id)
                .map(ApiResponse::ok)
                .orElse(ApiResponse.ok(null));
    }

    @Operation(summary = "查询所有租户", description = "Get all tenants",operationId = "listTenants")
    @GetMapping("/list")
    public ApiResponse<PageResult<TenantDTO>> getAllTenants(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size) {
        return ApiResponse.ok(tenantService.list(page,size));
    }

    @Operation(summary = "更新租户信息", description = "Update tenant info",operationId = "updateTenant")
    @PutMapping("/update")
    public ApiResponse<TenantDTO> updateTenant(@RequestBody  @Valid TenantDTO dto) {
        return ApiResponse.ok(tenantService.updateTenant(dto));
    }

    @Operation(summary = "删除租户", description = "Delete tenant",operationId = "deleteTenant")
    @DeleteMapping("/delete/{id}")
    public ApiResponse<Void> deleteTenant(@PathVariable Long id) {
        tenantService.sDelete(id);
        return ApiResponse.ok(null);
    }
}
