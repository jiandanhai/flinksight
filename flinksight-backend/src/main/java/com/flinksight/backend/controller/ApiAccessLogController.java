package com.flinksight.backend.controller;

import com.flinksight.backend.common.ApiResponse;
import com.flinksight.common.dto.ApiAccessLogDTO;
import com.flinksight.common.model.PageResult;
import com.flinksight.common.service.ApiAccessLogService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

/**
 * API访问日志管理
 */
@RestController
@RequestMapping("/api/api-access-log")
@RequiredArgsConstructor
public class ApiAccessLogController {

    private final ApiAccessLogService service;

    @Operation(summary = "", operationId = "createApiAccessLog")
    @PostMapping
    public ApiResponse<ApiAccessLogDTO> create(@RequestBody ApiAccessLogDTO apiAccessLogDTO) {
        return ApiResponse.ok(service.createOrUpdate(apiAccessLogDTO));
    }

    @Operation(summary = "", operationId = "getApiAccessLog")
    @GetMapping("/{id}")
    public ApiResponse<ApiAccessLogDTO> getById(@PathVariable Long id) {
        return service.getById(id)
                .map(ApiResponse::ok)
                .orElse(ApiResponse.ok(null));
    }

    @Operation(summary = "", operationId = "getAllApiAccessLogs")
    @GetMapping
    public ApiResponse<PageResult<ApiAccessLogDTO>> getAll(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size) {
        return ApiResponse.ok(service.getAll(page,size));
    }

    @Operation(summary = "", operationId = "getApiAccessLogsByTenant")
    @GetMapping("/tenant/{tenantId}")
    public ApiResponse<PageResult<ApiAccessLogDTO>> findByTenantId(
            @PathVariable Long tenantId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size) {
        return ApiResponse.ok(service.findByTenantId(tenantId,page,size));
    }

    @Operation(summary = "", operationId = "updateApiAccessLog")
    @PutMapping
    public ApiResponse<ApiAccessLogDTO> update(@RequestBody ApiAccessLogDTO dto) {
        return ApiResponse.ok(service.createOrUpdate(dto));
    }

    @Operation(summary = "", operationId = "deleteApiAccessLog")
    @DeleteMapping("/{id}")
    public boolean delete(@PathVariable Long id) {
        return service.softDelete(id);
    }
}
