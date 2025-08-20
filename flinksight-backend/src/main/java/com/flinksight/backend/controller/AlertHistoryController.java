package com.flinksight.backend.controller;

import com.flinksight.backend.common.ApiResponse;
import com.flinksight.common.dto.AlertHistoryDTO;
import com.flinksight.common.model.PageResult;
import com.flinksight.common.service.AlertHistoryService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

/**
 * 报警历史管理
 */
@RestController
@Tag(name = "api", description = "告警历史事件")
@RequestMapping("/api/alert/history")
@RequiredArgsConstructor
@Validated
public class AlertHistoryController {

    private final AlertHistoryService service;

    @Operation(summary = "创建报警历史事件", description = "Update alert info",operationId = "createAlertHistory")
    @PostMapping("/create")
    public ApiResponse<AlertHistoryDTO> create(@RequestBody  @Valid AlertHistoryDTO dto) {

        return ApiResponse.ok(service.createOrUpdate(dto));
    }

    @Operation(summary = "获取报警历史事件", description = "Update alert info",operationId = "getAlertHistory")
    @GetMapping("/id/{id}")
    public ApiResponse<AlertHistoryDTO> getById(@PathVariable Long id) {
        return service.getById(id)
                .map(ApiResponse::ok)
                .orElse(ApiResponse.ok(null));
    }

    @Operation(summary = "分页获取所有报警历史记录", operationId = "getAllAlertHistorys")
    @GetMapping("/list")
    public ApiResponse<PageResult<AlertHistoryDTO>> getAll(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size) {
        return ApiResponse.ok(service.getAll(page,size));
    }

    @Operation(summary = "分页获取所有报警历史记录", operationId = "getAlertHistorysByTenant")
    @GetMapping("/tenant/{tenantId}")
    public ApiResponse<PageResult<AlertHistoryDTO>> findByTenantId(
            @PathVariable Long tenantId ,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size) {
        return ApiResponse.ok(service.findByTenantId(tenantId, page,size));
    }

    @Operation(summary = "更新报警历史记录", operationId = "updateAlertHistory")
    @PutMapping("/update")
    public ApiResponse<AlertHistoryDTO> update(@RequestBody  @Valid AlertHistoryDTO dto) {

        return ApiResponse.ok(service.createOrUpdate(dto));
    }

    @Operation(summary = "更新报警历史记录", operationId = "deleteAlertHistory")
    @DeleteMapping("/delete/{id}")
    public boolean delete(@PathVariable Long id) {
        return service.softDelete(id);
    }
}
