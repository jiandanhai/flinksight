package com.flinksight.backend.controller;

import com.flinksight.backend.common.ApiResponse;
import com.flinksight.common.dto.AlertRuleDTO;
import com.flinksight.common.model.PageResult;
import com.flinksight.common.service.AlertRuleService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

/**
 * 报警规则管理接口
 */
@RestController
@RequestMapping("/api/alert-rule")
@Tag(name = "报警规则", description = "报警规则配置管理")
@RequiredArgsConstructor
public class AlertRuleController {

    private final AlertRuleService alertRuleService;

    @Operation(summary = "新建报警规则", operationId = "createAlertRule")
    @PostMapping("/create")
    public ApiResponse<AlertRuleDTO> create(@RequestBody AlertRuleDTO dto) {
        return ApiResponse.ok(alertRuleService.createAlertRule(dto));
    }

    @Operation(summary = "更新报警规则", operationId = "updateAlertRule")
    @PostMapping("/update")
    public ApiResponse<AlertRuleDTO> update(@RequestBody AlertRuleDTO dto) {
        return ApiResponse.ok(alertRuleService.updateAlertRule(dto));
    }

    @Operation(summary = "删除报警规则", operationId = "deleteAlertRule")
    @PostMapping("/delete")
    public ApiResponse<Boolean> delete(@RequestParam Long id) {
        return ApiResponse.ok(alertRuleService.softDelete(id));
    }

    @Operation(summary = "获取报警规则详情", operationId = "getAlertRuleById")
    @GetMapping("/get")
    public ApiResponse<AlertRuleDTO> get(@RequestParam Long id) {
        return alertRuleService.getAlertRuleById(id)
                .map(ApiResponse::ok)
                .orElse(ApiResponse.ok(null));
    }

    @Operation(summary = "获取租户下所有报警规则", operationId = "getAlertRulesByTenant")
    @GetMapping("/listByTenant")
    public ApiResponse<PageResult<AlertRuleDTO>> list(@RequestParam Long tenantId,
                                                      @RequestParam(defaultValue = "0") int page,
                                                      @RequestParam(defaultValue = "20") int size){
            return ApiResponse.ok(alertRuleService.getAlertRulesByTenant(tenantId,page,size));
    }

    @Operation(summary = "获取租户集群下所有报警规则", operationId = "getAlertRulesByTenantAndCluster")
    @GetMapping("/listByTenantAndCluster")
    public ApiResponse<PageResult<AlertRuleDTO>> listByCluster(@RequestParam Long tenantId,
                                                         @RequestParam Long clusterId,
                                                         @RequestParam(defaultValue = "0") int page,
                                                         @RequestParam(defaultValue = "20") int size) {
        return ApiResponse.ok(alertRuleService.listByTenantAndCluster(tenantId, clusterId,page,size));
    }
}
