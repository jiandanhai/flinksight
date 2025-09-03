package com.flinksight.backend.controller;

import com.flinksight.backend.audit.AuditLoggable;
import com.flinksight.backend.common.ApiResponse;
import com.flinksight.common.dto.AlertRuleDTO;
import com.flinksight.common.dto.RuleMatchResultDTO;
import com.flinksight.common.dto.RuleTestRequestDTO;
import com.flinksight.common.model.PageResult;
import com.flinksight.common.service.AlertRuleService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 报警规则管理接口
 *
 * 说明：
 * - /api/alert/rule/id/{id} → 获取单条详情
 * - /api/alert/rule/listByTenant → 按租户分页查询
 * - /api/alert/rule/listByTenantAndCluster → 按租户 + 集群分页查询
 * - /api/alert/rule/{id}/enable?value=true|false → 启停单条
 * - /api/alert/rule/enable-batch → 批量启停
 */
@RestController
@RequestMapping("/api/alert/rule")
@Tag(name = "api", description = "报警规则配置管理")
@RequiredArgsConstructor
@Validated
public class AlertRuleControllerv1 {

    private final AlertRuleService alertRuleService;

    // 规则：创建
    @AuditLoggable(action = "CREATE", target = "RULE", targetId = "#result.id", detail = "T(java.util.Objects).toString(#dto)") // 详情可序列化为字符串/JSON)
    @Operation(summary = "新建报警规则", operationId = "createAlertRule")
    @PostMapping("/create")
    public ApiResponse<AlertRuleDTO> createAlertRule(@RequestBody @Valid AlertRuleDTO dto) {
        return ApiResponse.ok(alertRuleService.createAlertRule(dto));
    }

    // 规则：更新
    @AuditLoggable(action = "UPDATE", target = "RULE", targetId = "#id", detail = "T(java.util.Objects).toString(#dto)")
    @Operation(summary = "更新报警规则", operationId = "updateAlertRule")
    @PostMapping("/update/{id}")
    public ApiResponse<AlertRuleDTO> updateAlertRule(@PathVariable Long id, @RequestBody @Valid AlertRuleDTO dto) {
        return ApiResponse.ok(alertRuleService.updateAlertRule(id, dto));
    }

    @Operation(summary = "删除报警规则", operationId = "deleteAlertRule")
    @PostMapping("/delete/{id}")
    public ApiResponse<Boolean> deleteAlertRule(@PathVariable Long id) {
        return ApiResponse.ok(alertRuleService.sDelete(id));
    }

    @Operation(summary = "批量删除规则（软删除）", operationId = "deleteBatch")
    @DeleteMapping
    public ApiResponse<Void> deleteBatch(@RequestBody List<Long> ids) {
        alertRuleService.deleteAlertRules(ids);
        return ApiResponse.ok(null);
    }

    @Operation(summary = "获取报警规则详情", operationId = "getAlertRule")
    @GetMapping("/id/{id}")
    public ApiResponse<AlertRuleDTO> getAlertRule(@PathVariable Long id) {
        return ApiResponse.ok(alertRuleService.getAlertRule(id));
    }

    @Operation(summary = "获取租户集群下所有报警规则", operationId = "listAlertRules")
    @GetMapping("/listAlertRules")
    public ApiResponse<PageResult<AlertRuleDTO>> list(@RequestParam(required = false) Long clusterId,
                                                      @RequestParam(required = false) Integer enable,
                                                      @RequestParam(required = false) String keyword,
                                                      @RequestParam(defaultValue = "0") int page,
                                                      @RequestParam(defaultValue = "20") int size) {
        return ApiResponse.ok(alertRuleService.list(clusterId, enable,keyword,page, size));
    }

    // 告警：启停单条
    @AuditLoggable(action = "STATUS_CHANGE", target = "ALERT", targetId = "#id", detail = "'enable=' + #enable")
    @Operation(summary = "启停规则（单条）", operationId = "enableOne")
    @PostMapping("/enable/{id}")
    public ApiResponse<AlertRuleDTO> enableOne(@PathVariable Long id,
                                               @RequestParam("value") boolean enable) {
        return ApiResponse.ok(alertRuleService.setEnable(id, enable));
    }

    @Operation(summary = "启停规则（批量）", operationId = "enableBatch")
    @PostMapping("/enable-batch")
    public ApiResponse<Void> enableBatch(@RequestParam("enable") boolean enable,
                                         @RequestBody List<Long> ids) {
        alertRuleService.setEnableBatch(ids, enable);
        return ApiResponse.ok(null);
    }

    @Operation(summary = "规则测试匹配：输入样例事件，返回最可能命中的规则（含评分与解释）")
    @PostMapping("/test-match")
    public ApiResponse<List<RuleMatchResultDTO>> testMatch(@RequestBody RuleTestRequestDTO req) {
        return ApiResponse.ok(alertRuleService.testMatch(req));
    }
}
