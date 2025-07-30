package com.flinksight.backend.controller;

import com.flinksight.backend.domain.AuditLog;
import com.flinksight.common.dto.AuditLogDTO;
import com.flinksight.common.service.AuditLogService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Optional;

/**
 * 审计日志管理
 */
@RestController
@RequestMapping("/api/audit-log")
@RequiredArgsConstructor
public class AuditLogController {

    private final AuditLogService service;

    @PostMapping
    public AuditLogDTO create(@RequestBody AuditLogDTO dto) {
        return service.createAuditLog(dto);
    }

    @GetMapping("/{id}")
    public Optional<AuditLogDTO> get(@PathVariable Long id) {
        return service.getAuditLogById(id);
    }

    @GetMapping("/tenant/{tenantId}")
    public List<AuditLogDTO> findByTenantId(@PathVariable Long tenantId,@RequestParam Long userId) {
        return service.getLogsByTenantAndUser(tenantId,userId);
    }

    @PutMapping
    public AuditLogDTO update(@RequestBody AuditLogDTO dto) {
        return service.createAuditLog(dto);
    }

    @DeleteMapping("/{id}")
    public boolean delete(@PathVariable Long id) {
        return service.softDelete(id);
    }
}
