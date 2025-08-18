package com.flinksight.backend.controller;

import com.flinksight.backend.common.ApiResponse;
import com.flinksight.common.dto.ResourceDTO;
import com.flinksight.common.model.PageResult;
import com.flinksight.common.service.ResourceService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

/**
 * 资源管理
 */
@Tag(name = "api",description = "资源管理API")
@RestController
@RequestMapping("/api/resource")
@RequiredArgsConstructor
@Validated
public class ResourceController {

    private final ResourceService service;

    @Operation(summary = "", description = "",operationId = "createResource")
    @PostMapping
    public ApiResponse<ResourceDTO> create(@RequestBody ResourceDTO dto) {

        return ApiResponse.ok(service.createOrUpdate(dto));
    }

    @Operation(summary = "", description = "",operationId = "getResource")
    @GetMapping("/{id}")
    public ApiResponse<ResourceDTO> getById(@PathVariable Long id) {
        return service.getById(id)
                .map(ApiResponse::ok)
                .orElse(ApiResponse.ok(null));
    }

    @Operation(summary = "", description = "",operationId = "getAllResources")
    @GetMapping
    public ApiResponse<PageResult<ResourceDTO>> getAll(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size) {
        return ApiResponse.ok(service.getAll(page,size));
    }

    @Operation(summary = "", description = "",operationId = "getResourcesByTenant")
    @GetMapping("/tenant/{tenantId}")
    public ApiResponse<PageResult<ResourceDTO>> findByTenantId(
            @PathVariable Long tenantId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size){
        return ApiResponse.ok(service.findByTenantId(tenantId,page,size));
    }

    @Operation(summary = "", description = "",operationId = "updateResource")
    @PutMapping
    public ApiResponse<ResourceDTO> update(@RequestBody ResourceDTO dto) {

        return ApiResponse.ok(service.createOrUpdate(dto));
    }

    @Operation(summary = "", description = "",operationId = "deleteResource")
    @DeleteMapping("/{id}")
    public boolean delete(@PathVariable Long id) {
        return service.softDelete(id);
    }
}
