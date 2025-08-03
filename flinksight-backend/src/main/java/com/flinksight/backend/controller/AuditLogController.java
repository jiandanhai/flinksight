package com.flinksight.backend.controller;

import com.flinksight.backend.common.ApiResponse;
import com.flinksight.common.dto.AuditLogDTO;
import com.flinksight.common.model.PageResult;
import com.flinksight.common.service.AuditLogService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

/**
 * 审计日志管理
 */
@RestController
@RequestMapping("/api/audit-log")
@RequiredArgsConstructor
public class AuditLogController {

    private final AuditLogService service;

    @PostMapping
    public ApiResponse<AuditLogDTO> create(@RequestBody AuditLogDTO dto) {

        return ApiResponse.ok(service.createAuditLog(dto));
    }

    @GetMapping("/{id}")
    public ApiResponse<AuditLogDTO> get(@PathVariable Long id) {
        return service.getAuditLogById(id)
                .map(ApiResponse::ok)
                .orElse(ApiResponse.ok(null));
    }

    @GetMapping("/tenant/{tenantId}")
    public ApiResponse<PageResult<AuditLogDTO>> findByTenantId(
            @PathVariable Long tenantId,
            @RequestParam Long userId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size) {
        return ApiResponse.ok(service.getLogsByTenantAndUser(tenantId,userId,page,size));
    }

    @PutMapping
    public ApiResponse<AuditLogDTO> update(@RequestBody AuditLogDTO dto) {

        return ApiResponse.ok(service.createAuditLog(dto));
    }

    @DeleteMapping("/{id}")
    public boolean delete(@PathVariable Long id) {
        return service.softDelete(id);
    }
}
