package com.flinksight.backend.controller;

import com.flinksight.backend.common.ApiResponse;
import com.flinksight.common.dto.OperationTemplateDTO;
import com.flinksight.common.model.PageResult;
import com.flinksight.common.service.OperationTemplateService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

/**
 * 操作模板管理
 */
@RestController
@RequestMapping("/api/operation-template")
@RequiredArgsConstructor
public class OperationTemplateController {

    private final OperationTemplateService service;

    @Operation(summary = "", description = "",operationId = "createOperationTemplate")
    @PostMapping
    public ApiResponse<OperationTemplateDTO> create(@RequestBody OperationTemplateDTO dto) {
        return ApiResponse.ok(service.createOrUpdate(dto));
    }

    @Operation(summary = "", description = "",operationId = "getOperationTemplate")
    @GetMapping("/{id}")
    public ApiResponse<OperationTemplateDTO> getById(@PathVariable Long id) {
        return service.getById(id)
                .map(ApiResponse::ok)
                .orElse(ApiResponse.ok(null));
    }

    @Operation(summary = "", description = "",operationId = "getAllOperationTemplates")
    @GetMapping
    public ApiResponse<PageResult<OperationTemplateDTO>> getAll(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size) {
        return ApiResponse.ok(service.getAll(page,size));
    }

    @Operation(summary = "", description = "",operationId = "getOperationTemplatesByTenant")
    @GetMapping("/tenant/{tenantId}")
    public ApiResponse<PageResult<OperationTemplateDTO>> findByTenantId(
            @PathVariable Long tenantId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size) {
        return ApiResponse.ok(service.findByTenantId(tenantId,page,size));
    }

    @Operation(summary = "", description = "",operationId = "updateOperationTemplate")
    @PutMapping
    public ApiResponse<OperationTemplateDTO> update(@RequestBody OperationTemplateDTO dto) {
        return ApiResponse.ok(service.createOrUpdate(dto));
    }

    @Operation(summary = "", description = "",operationId = "deleteOperationTemplate")
    @DeleteMapping("/{id}")
    public boolean delete(@PathVariable Long id) {
        return service.softDelete(id);
    }
}
