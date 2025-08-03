package com.flinksight.backend.controller;

import com.flinksight.backend.common.ApiResponse;
import com.flinksight.common.dto.ResourceGroupDTO;
import com.flinksight.common.model.PageResult;
import com.flinksight.common.service.ResourceGroupService;
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

    @PostMapping
    public ApiResponse<ResourceGroupDTO> create(@RequestBody ResourceGroupDTO dto) {
        return ApiResponse.ok(service.createOrUpdate(dto));
    }

    @GetMapping("/{id}")
    public ApiResponse<ResourceGroupDTO> get(@PathVariable Long id) {
        return service.getById(id)
                .map(ApiResponse::ok)
                .orElse(ApiResponse.ok(null));
    }

    @GetMapping
    public ApiResponse<PageResult<ResourceGroupDTO>> getAll(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size) {
        return ApiResponse.ok(service.getAll(page,size));
    }

    @GetMapping("/tenant/{tenantId}")
    public ApiResponse<PageResult<ResourceGroupDTO>> findByTenantId(
            @PathVariable Long tenantId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size) {
        return ApiResponse.ok(service.findByTenantId(tenantId,page,size));
    }

    @PutMapping
    public ApiResponse<ResourceGroupDTO> update(@RequestBody ResourceGroupDTO dto) {
        return ApiResponse.ok(service.createOrUpdate(dto));
    }

    @DeleteMapping("/{id}")
    public boolean delete(@PathVariable Long id) {
        return service.softDelete(id);
    }
}
