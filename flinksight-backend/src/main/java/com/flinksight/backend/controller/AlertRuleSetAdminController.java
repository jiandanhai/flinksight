// src/main/java/com/flinksight/backend/controller/AlertRuleSetAdminController.java
package com.flinksight.backend.controller;


import com.flinksight.backend.audit.AuditLoggable;
import com.flinksight.common.dto.AlertRuleItemDTO;
import com.flinksight.common.dto.RuleSetDetailDTO;
import com.flinksight.common.dto.RuleSetSummaryDTO;
import com.flinksight.common.enums.RuleSetStatus;
import com.flinksight.common.enums.ScopeType;
import com.flinksight.common.service.AlertRuleAdminService;
import com.flinksight.common.service.AlertRuleSetAdminService;
import com.flinksight.common.service.AlertRuleSetQueryService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.constraints.Min;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@Tag(name = "api", description = "规则集版本生命周期管理（动作型 API）")
@RestController
@RequestMapping("/api/admin/rule-sets")
@RequiredArgsConstructor
@Validated
public class AlertRuleSetAdminController {

  private final AlertRuleSetQueryService queryService;
  private final AlertRuleSetAdminService adminService;
  private final AlertRuleAdminService draftService; // createDraft/replaceItems/activate 已在此实现

  @Operation(summary = "规则集版本列表", description = "分页检索规则集，支持按租户、作用域、状态、是否激活过滤")
  @ApiResponses(@ApiResponse(responseCode = "200", description = "OK",
      content = @Content(schema = @Schema(implementation = RuleSetSummaryDTO.class))))
  @GetMapping
  public ResponseEntity<Page<RuleSetSummaryDTO>> list(
      @Parameter(description = "租户ID") @RequestParam(required = false) Long tenantId,
      @Parameter(description = "作用域类型") @RequestParam(required = false) ScopeType scopeType,
      @Parameter(description = "作用域ID") @RequestParam(required = false) Long scopeId,
      @Parameter(description = "状态") @RequestParam(required = false) RuleSetStatus status,
      @Parameter(description = "是否激活(0/1)") @RequestParam(required = false) Integer activeFlag,
      @RequestParam(defaultValue = "0") @Min(0) int page,
      @RequestParam(defaultValue = "20") @Min(1) int size,
      @RequestParam(defaultValue = "version,desc") String sort
  ) {
    Sort s = parseSort(sort);
    Pageable pageable = PageRequest.of(page, size, s);
    return ResponseEntity.ok(queryService.listVersions(tenantId, scopeType, scopeId, status, activeFlag, pageable));
  }

  @Operation(summary = "规则集详情", description = "查看某个规则集版本的元数据与规则项")
  @GetMapping("/{id}")
  public ResponseEntity<RuleSetDetailDTO> detail(@PathVariable Long id) {
    return ResponseEntity.ok(queryService.detail(id));
  }

  @AuditLoggable(action = "CREATE", target = "AlertRuleSet", targetId = "#result.id") // 详情可序列化为字符串/JSON)
  @Operation(summary = "创建草稿版本",operationId = "createDraft")
  @PostMapping
  public ResponseEntity<Long> createDraft(
      @RequestParam ScopeType scopeType,
      @RequestParam(required = false) Long scopeId,
      @RequestParam Long version,
      @RequestParam(defaultValue = "system") String operator) {
    Long id = draftService.createDraft(scopeType, scopeId, version, operator);
    return ResponseEntity.status(HttpStatus.CREATED).body(id);
  }

  @Operation(summary = "替换草稿规则项（幂等）")
  @PutMapping("/{id}/items")
  public ResponseEntity<Void> replaceItems(@PathVariable Long id,
                                           @io.swagger.v3.oas.annotations.parameters.RequestBody(
                                             required = true,
                                             content = @Content(array = @ArraySchema(schema = @Schema(implementation = com.flinksight.backend.domain.AlertRuleItem.class)))
                                           )
                                           @RequestBody java.util.List<AlertRuleItemDTO> items) {
    draftService.replaceItems(id, items);
    return ResponseEntity.noContent().build();
  }

  @Operation(summary = "激活版本（原子切换当前 ACTIVE）")
  @PostMapping("/{id}/activate")
  public ResponseEntity<Void> activate(@PathVariable Long id) {
    draftService.activate(id);
    return ResponseEntity.noContent().build();
  }

  @Operation(summary = "删除草稿版本（仅 DRAFT）")
  @DeleteMapping("/{id}")
  public ResponseEntity<Void> deleteDraft(@PathVariable Long id) {
    adminService.deleteDraft(id);
    return ResponseEntity.noContent().build();
  }

  @Operation(summary = "归档当前版本（ACTIVE→ARCHIVED）")
  @PostMapping("/{id}/archive")
  public ResponseEntity<Void> archive(@PathVariable Long id) {
    adminService.archive(id);
    return ResponseEntity.noContent().build();
  }

  @Operation(summary = "回滚到指定历史版本（激活它）")
  @PostMapping("/{id}/rollback")
  public ResponseEntity<Void> rollback(@PathVariable Long id) {
    adminService.rollbackTo(id);
    return ResponseEntity.noContent().build();
  }

  private static Sort parseSort(String expr) {
    if (expr == null || expr.isBlank()) return Sort.by(Sort.Order.desc("version"));
    String[] parts = expr.split(",");
    if (parts.length == 2) {
      return Sort.by(new Sort.Order("desc".equalsIgnoreCase(parts[1]) ? Sort.Direction.DESC : Sort.Direction.ASC, parts[0]));
    }
    return Sort.by(Sort.Order.by(expr));
  }
}
