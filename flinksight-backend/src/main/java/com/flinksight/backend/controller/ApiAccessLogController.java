package com.flinksight.backend.controller;

import com.flinksight.backend.common.ApiResponse;
import com.flinksight.common.dto.ApiAccessLogDTO;
import com.flinksight.common.model.PageResult;
import com.flinksight.common.service.ApiAccessLogService;
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

    @PostMapping
    public ApiResponse<ApiAccessLogDTO> create(@RequestBody ApiAccessLogDTO apiAccessLogDTO) {
        return ApiResponse.ok(service.createOrUpdate(apiAccessLogDTO));
    }

    @GetMapping("/{id}")
    public ApiResponse<ApiAccessLogDTO> get(@PathVariable Long id) {
        return service.getById(id)
                .map(ApiResponse::ok)
                .orElse(ApiResponse.ok(null));
    }

    @GetMapping
    public ApiResponse<PageResult<ApiAccessLogDTO>> getAll(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size) {
        return ApiResponse.ok(service.getAll(page,size));
    }

    @GetMapping("/tenant/{tenantId}")
    public ApiResponse<PageResult<ApiAccessLogDTO>> findByTenantId(
            @PathVariable Long tenantId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size) {
        return ApiResponse.ok(service.findByTenantId(tenantId,page,size));
    }

    @PutMapping
    public ApiResponse<ApiAccessLogDTO> update(@RequestBody ApiAccessLogDTO dto) {
        return ApiResponse.ok(service.createOrUpdate(dto));
    }

    @DeleteMapping("/{id}")
    public boolean delete(@PathVariable Long id) {
        return service.softDelete(id);
    }
}
