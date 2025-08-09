package com.flinksight.backend.controller;

import com.flinksight.backend.common.ApiResponse;
import com.flinksight.common.dto.ApiKeyDTO;
import com.flinksight.common.model.PageResult;
import com.flinksight.common.service.ApiKeyService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

/**
 * API密钥管理
 */
@RestController
@RequestMapping("/api/api-key")
@RequiredArgsConstructor
public class ApiKeyController {

    private final ApiKeyService service;

    @Operation(summary = "", operationId = "createApiKey")
    @PostMapping
    public ApiKeyDTO create(@RequestBody ApiKeyDTO dto) {
        return service.createOrUpdate(dto);
    }

    @Operation(summary = "", operationId = "getApiKey")
    @GetMapping("/{id}")
    public ApiResponse<ApiKeyDTO> getById(@PathVariable Long id) {
        return service.getById(id)
                .map(ApiResponse::ok)
                .orElse(ApiResponse.ok(null));
    }

    @Operation(summary = "", operationId = "getAllApiKeys")
    @GetMapping
    public ApiResponse<PageResult<ApiKeyDTO>> getAll(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size) {
        return ApiResponse.ok(service.getAll(page,size));
    }

    @Operation(summary = "", operationId = "getApiKeysByTenant")
    @GetMapping("/tenant/{tenantId}")
    public ApiResponse<PageResult<ApiKeyDTO>> findByTenantId(
            @PathVariable Long tenantId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size) {
        return ApiResponse.ok(service.findByTenantId(tenantId,page,size));
    }

    @Operation(summary = "", operationId = "updateApiKey")
    @PutMapping
    public ApiResponse<ApiKeyDTO> update(@RequestBody ApiKeyDTO dto) {

        return ApiResponse.ok(service.createOrUpdate(dto));
    }

    @Operation(summary = "", operationId = "deleteApiKey")
    @DeleteMapping("/{id}")
    public boolean delete(@PathVariable Long id) {
        return service.softDelete(id);
    }
}
