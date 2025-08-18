package com.flinksight.backend.controller;

import com.flinksight.backend.common.ApiResponse;
import com.flinksight.common.dto.AuditLogDTO;
import com.flinksight.common.model.PageResult;
import com.flinksight.common.service.AuditLogService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

/**
 * 审计日志管理
 */
@RestController
@Tag(name = "api", description = "审计日志管理")
@RequestMapping("/api/audit-log")
@RequiredArgsConstructor
@Validated
public class AuditLogController {

    private final AuditLogService service;

    @Operation(summary = "分页获取所有报警历史记录", operationId = "createAuditLog")
    @PostMapping
    public ApiResponse<AuditLogDTO> create(@RequestBody AuditLogDTO dto) {

        return ApiResponse.ok(service.createAuditLog(dto));
    }

    @Operation(summary = "", operationId = "getAuditLog")
    @GetMapping("/{id}")
    public ApiResponse<AuditLogDTO> getById(@PathVariable Long id) {
        return service.getAuditLogById(id)
                .map(ApiResponse::ok)
                .orElse(ApiResponse.ok(null));
    }

    @Operation(summary = "", operationId = "getAuditLogsByTenant")
    @GetMapping("/tenant/{tenantId}")
    public ApiResponse<PageResult<AuditLogDTO>> findByTenantId(
            @PathVariable Long tenantId,
            @RequestParam Long userId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size) {
        return ApiResponse.ok(service.getLogsByTenantAndUser(tenantId,userId,page,size));
    }

    @Operation(summary = "", operationId = "updateAuditLog")
    @PutMapping
    public ApiResponse<AuditLogDTO> update(@RequestBody AuditLogDTO dto) {

        return ApiResponse.ok(service.createAuditLog(dto));
    }

    @Operation(summary = "", operationId = "deleteAuditLog")
    @DeleteMapping("/{id}")
    public boolean delete(@PathVariable Long id) {
        return service.softDelete(id);
    }
}
