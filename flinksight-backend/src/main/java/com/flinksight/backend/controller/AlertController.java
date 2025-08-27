package com.flinksight.backend.controller;

import com.flinksight.backend.common.ApiResponse;
import com.flinksight.backend.security.tenant.TenantRequired;
import com.flinksight.common.dto.AlertDTO;
import com.flinksight.common.dto.AlertHistoryDTO;
import com.flinksight.common.dto.AlertOpsDTO;
import com.flinksight.common.dto.AlertRuleDTO;
import com.flinksight.common.model.PageResult;
import com.flinksight.common.service.AlertService;
import com.flinksight.common.service.OpAudit;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ContentDisposition;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.io.ByteArrayOutputStream;
import java.nio.charset.StandardCharsets;
import java.util.List;

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


    @Operation(summary = "根据ID查询报警事件", operationId = "getAlert")
    @GetMapping("/id/{id}")
    public ApiResponse<AlertDTO> getAlert(@PathVariable Long id) {
        return ApiResponse.ok(alertService.getAlert(id));
    }

    @Operation(summary = "获取告警匹配的规则集", operationId = "getAlertRules")
    @GetMapping("/{id}/rules")
    public ApiResponse<List<AlertRuleDTO>> getAlertRules(@PathVariable Long id) {
        return ApiResponse.ok(alertService.getAlertRules(id));
    }

    @Operation(summary = "获取告警历史", operationId = "getAlertHistory")
    @GetMapping("/{id}/history")
    public ApiResponse<List<AlertHistoryDTO>> getAlertHistory(@PathVariable Long id) {
        return ApiResponse.ok(alertService.getAlertHistory(id));
    }

    @Operation(summary = "获取告警可执行操作", operationId = "getAlertOps")
    @GetMapping("/{id}/ops")
    public ApiResponse<AlertOpsDTO> getAlertOps(@PathVariable Long id) {
        return ApiResponse.ok(alertService.getAlertOps(id));
    }

    @Operation(summary = "查询任务下报警事件", description = "Get alerts by job and status",operationId = "listAlerts")
    @GetMapping("/listAlerts")
    public ApiResponse<PageResult<AlertDTO>> getAlertsByLevelAndStatus(
            @RequestParam Long jobId,
            @RequestParam String level,
            @RequestParam Integer status,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size) {
        return ApiResponse.ok(alertService.list(jobId,level, status, page,size));
    }

    @OpAudit(action = "UPDATE", targetType = "ALERT", targetIdSpEL = "#id", contentSpEL = "'payload=' + T(com.flinksight.common.util.Jsons).toJson(#dto)")
    @Operation(summary = "更新报警事件,部分更新（PATCH 语义）", description = "Update alert info",operationId = "updateAlert")
    @PutMapping("/update/{id}")
    public ApiResponse<AlertDTO> updateAlert(@PathVariable Long id,@RequestBody  @Valid AlertDTO alert) {
        return ApiResponse.ok(alertService.updateAlert(id,alert));
    }

    @Operation(summary = "删除报警事件（软删）", description = "Soft delete alert",operationId = "deleteAlert")
    @DeleteMapping("/delete/{id}")
    public ApiResponse<Void> deleteAlert(@PathVariable Long id) {
        alertService.sDelete(id);
        return ApiResponse.ok(null);
    }

    @Operation(summary = "批量删除（软删除）",operationId = "deleteBatch")
    @DeleteMapping
    public ApiResponse<Void> deleteBatch(@RequestBody List<Long> ids) {
        alertService.deleteAlerts(ids);
        return ApiResponse.ok(null);
    }

    // 导出也能记
    @OpAudit(action = "EXPORT", targetType = "Alert", contentSpEL = "'filters: level='+#level+', status='+#status")
    @Operation(summary = "导出CSV（按级别/状态过滤）",operationId = "exportAlerts")
    @GetMapping(value = "/export", produces = "text/csv")
    public ResponseEntity<byte[]> export(
            @RequestParam(required = false) String level,
            @RequestParam(required = false) Integer status) {
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        alertService.exportAlerts(level, status, bos);
        byte[] bytes = bos.toByteArray();
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.parseMediaType("text/csv; charset=UTF-8"));
        headers.setContentDisposition(
                ContentDisposition.attachment()
                        .filename("alerts.csv", StandardCharsets.UTF_8)
                        .build()
        );
        headers.setContentLength(bytes.length);
        return ResponseEntity.ok().headers(headers).body(bytes);
    }

}
