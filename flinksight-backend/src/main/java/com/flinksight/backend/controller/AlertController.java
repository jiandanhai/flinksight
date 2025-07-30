package com.flinksight.backend.controller;

import com.flinksight.backend.domain.Alert;
import com.flinksight.backend.security.tenant.TenantRequired;
import com.flinksight.common.dto.AlertDTO;
import com.flinksight.common.service.AlertService;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 报警事件接口
 */
@Tag(name = "报警事件", description = "Alert API")
@RestController
@RequestMapping("/api/alert")
@RequiredArgsConstructor
@TenantRequired
public class AlertController {

    private final AlertService alertService;

    @Operation(summary = "新建报警事件", description = "Create alert")
    @PostMapping("/create")
    public ResponseEntity<AlertDTO> createAlert(@RequestBody AlertDTO alert) {
        return ResponseEntity.ok(alertService.createAlert(alert));
    }

    @Operation(summary = "根据ID查询报警事件", description = "Get alert by ID")
    @GetMapping("/{id}")
    public ResponseEntity<AlertDTO> getAlertById(@PathVariable Long id) {
        return alertService.getAlertById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @Operation(summary = "查询租户下报警事件", description = "Get alerts by tenant and status")
    @GetMapping("/listByTenant")
    public ResponseEntity<List<AlertDTO>> getAlertsByTenantAndStatus(
            @RequestParam Long tenantId, @RequestParam Integer status) {
        return ResponseEntity.ok(alertService.getAlertsByTenantAndStatus(tenantId, status));
    }

    @Operation(summary = "查询任务下报警事件", description = "Get alerts by job and status")
    @GetMapping("/listByJob")
    public ResponseEntity<List<AlertDTO>> getAlertsByJobAndStatus(
            @RequestParam Long jobId, @RequestParam Integer status) {
        return ResponseEntity.ok(alertService.getAlertsByJobAndStatus(jobId, status));
    }

    @Operation(summary = "更新报警事件", description = "Update alert info")
    @PutMapping("/update")
    public ResponseEntity<AlertDTO> updateAlert(@RequestBody AlertDTO alert) {
        return ResponseEntity.ok(alertService.updateAlert(alert));
    }

    @Operation(summary = "删除报警事件（软删）", description = "Soft delete alert")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteAlert(@PathVariable Long id) {
        alertService.softDelete(id);
        return ResponseEntity.ok().build();
    }
}
