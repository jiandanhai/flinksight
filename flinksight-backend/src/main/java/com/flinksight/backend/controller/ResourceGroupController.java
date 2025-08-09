package com.flinksight.backend.controller;

import com.flinksight.backend.common.ApiResponse;
import com.flinksight.common.dto.ResourceGroupDTO;
import com.flinksight.common.model.PageResult;
import com.flinksight.common.service.ResourceGroupService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

/**
 * 资源分组管理
 */
@RestController
@RequestMapping("/api/resource-group")
@RequiredArgsConstructor
public class ResourceGroupController {

    private final ResourceGroupService service;

    @Operation(summary = "", description = "",operationId = "createResourceGroup")
    @PostMapping
    public ApiResponse<ResourceGroupDTO> create(@RequestBody ResourceGroupDTO dto) {
        return ApiResponse.ok(service.createOrUpdate(dto));
    }

    @Operation(summary = "", description = "",operationId = "getResourceGroup")
    @GetMapping("/{id}")
    public ApiResponse<ResourceGroupDTO> getById(@PathVariable Long id) {
        return service.getById(id)
                .map(ApiResponse::ok)
                .orElse(ApiResponse.ok(null));
    }

    @Operation(summary = "", description = "",operationId = "getAllResourceGroups")
    @GetMapping
    public ApiResponse<PageResult<ResourceGroupDTO>> getAll(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size) {
        return ApiResponse.ok(service.getAll(page,size));
    }

    @Operation(summary = "", description = "",operationId = "getResourceGroupsByTenant")
    @GetMapping("/tenant/{tenantId}")
    public ApiResponse<PageResult<ResourceGroupDTO>> findByTenantId(
            @PathVariable Long tenantId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size) {
        return ApiResponse.ok(service.findByTenantId(tenantId,page,size));
    }

    @Operation(summary = "", description = "",operationId = "updateResourceGroup")
    @PutMapping
    public ApiResponse<ResourceGroupDTO> update(@RequestBody ResourceGroupDTO dto) {
        return ApiResponse.ok(service.createOrUpdate(dto));
    }

    @Operation(summary = "", description = "",operationId = "deleteResourceGroup")
    @DeleteMapping("/{id}")
    public boolean delete(@PathVariable Long id) {
        return service.softDelete(id);
    }
}
