package com.flinksight.backend.controller;

import com.flinksight.backend.common.ApiResponse;
import com.flinksight.backend.security.tenant.TenantRequired;
import com.flinksight.common.dto.AlertDTO;
import com.flinksight.common.model.PageResult;
import com.flinksight.common.service.AlertService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

/**
 * 报警事件接口
 */
@Tag(name = "api", description = "告警事件")
@RestController
@RequestMapping("/api/alert")
@RequiredArgsConstructor
@TenantRequired
@Validated
public class AlertController {

    private final AlertService alertService;

    @Operation(summary = "新建报警事件", operationId = "createAlert")
    @PostMapping("/create")
    public ApiResponse<AlertDTO> createAlert(@RequestBody AlertDTO alert) {
        return ApiResponse.ok(alertService.createAlert(alert));
    }

    @Operation(summary = "根据ID查询报警事件", operationId = "getAlert")
    @GetMapping("/{id}")
    public ApiResponse<AlertDTO> getAlertById(@PathVariable Long id) {
        return alertService.getAlertById(id)
                .map(ApiResponse::ok)
                .orElse(ApiResponse.ok(null));
    }

    @Operation(summary = "查询租户下报警事件", description = "Get alerts by tenant and status",operationId = "getAlertsByTenantAndStatus")
    @GetMapping("/listByTenantAndStatus")
    public ApiResponse<PageResult<AlertDTO>> getAlertsByTenantAndStatus(
            @RequestParam Long tenantId,
            @RequestParam Integer status,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size) {
        return ApiResponse.ok(alertService.getAlertsByTenantAndStatus(tenantId, status, page,size));
    }

    @Operation(summary = "查询任务下报警事件", description = "Get alerts by job and status",operationId = "getAlertsByJobAndStatus")
    @GetMapping("/listByJobAndStatus")
    public ApiResponse<PageResult<AlertDTO>> getAlertsByJobAndStatus(
            @RequestParam Long jobId,
            @RequestParam Integer status,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size) {
        return ApiResponse.ok(alertService.getAlertsByJobAndStatus(jobId, status, page,size));
    }

    @Operation(summary = "查询任务下报警事件", description = "Get alerts by job and status",operationId = "getAlertsByLevelAndStatus")
    @GetMapping("/listByLevelAndStatus")
    public ApiResponse<PageResult<AlertDTO>> getAlertsByLevelAndStatus(
            @RequestParam String level,
            @RequestParam Integer status,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size) {
        return ApiResponse.ok(alertService.getAlertsByLevelAndStatus(level, status, page,size));
    }

    @Operation(summary = "更新报警事件", description = "Update alert info",operationId = "updateAlert")
    @PutMapping("/update")
    public ApiResponse<AlertDTO> updateAlert(@RequestBody AlertDTO alert) {
        return ApiResponse.ok(alertService.updateAlert(alert));
    }

    @Operation(summary = "删除报警事件（软删）", description = "Soft delete alert",operationId = "deleteAlert")
    @DeleteMapping("/{id}")
    public ApiResponse<Void> deleteAlert(@PathVariable Long id) {
        alertService.softDelete(id);
        return ApiResponse.ok(null);
    }
}
