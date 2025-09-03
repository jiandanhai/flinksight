// src/main/java/com/flinksight/backend/controller/AlertRuleItemsController.java
package com.flinksight.backend.controller;

import com.flinksight.common.dto.*;
import com.flinksight.common.enums.ScopeType;
import com.flinksight.common.service.AlertRuleAdminService;
import com.flinksight.common.service.AlertRuleQueryService;
import com.flinksight.common.service.AlertRuleSetQueryService;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@Tag(name = "api", description = "规则项的列表/编辑/删除/批量启用（仅作用于 DRAFT 版本）")
@RestController
@RequestMapping("/api/alert/rule-sets/{ruleSetId}/draft/items")
@RequiredArgsConstructor
@Validated
public class AlertRuleItemsController {

  private final AlertRuleAdminService alertRuleAdminService;
  private final AlertRuleQueryService alertRuleQueryService;
  private final AlertRuleSetQueryService alertRuleSetQueryService;

  /** 作业侧：拉取快照（ETag 支持增量） */
  @GetMapping("/rules/snapshot")
  public ResponseEntity<RuleSnapshotDTO> snapshot(@RequestParam @NotNull ScopeType scopeType,
                                                  @RequestParam(required=false) Long scopeId,
                                                  @RequestHeader(value="If-None-Match", required=false) String etag) {
    RuleSnapshotDTO snap = alertRuleQueryService.snapshot(scopeType, scopeId);
    if (etag != null && etag.equals(snap.getEtag())) {
      return ResponseEntity.status(HttpStatus.NOT_MODIFIED).build();
    }
    return ResponseEntity.ok()
            .eTag(snap.getEtag())
            .body(snap);
  }

  /** 规则列表（分页 + 可按 enabled 过滤） */
  @GetMapping
  public ResponseEntity<Page<AlertRuleItemDTO>> list(
      @PathVariable Long ruleSetId,
      @RequestParam(required = false) Integer enabled,
      @RequestParam(defaultValue = "0") @Min(0) int page,
      @RequestParam(defaultValue = "20") @Min(1) int size,
      @RequestParam(defaultValue = "metricKey,asc") String sort // 例如 "severity,desc"
  ) {
    Sort s = Sort.by(
        sort.contains(",")
            ? Sort.Order.by(sort.split(",")[0]).with(
                "desc".equalsIgnoreCase(sort.split(",")[1]) ? Sort.Direction.DESC : Sort.Direction.ASC
              )
            : Sort.Order.asc(sort)
    );
    Pageable pageable = PageRequest.of(page, size, s);
    return ResponseEntity.ok(alertRuleQueryService.list(ruleSetId, enabled, pageable));
  }

  /** 规则详情 */
  @GetMapping("/{itemId}")
  public ResponseEntity<AlertRuleItemDTO> get(@PathVariable Long ruleSetId, @PathVariable Long itemId) {
    return ResponseEntity.ok(alertRuleQueryService.get(ruleSetId, itemId));
  }

  /** 编辑（全量更新）—仅 DRAFT 可改 */
  @PutMapping("/{itemId}")
  public ResponseEntity<AlertRuleItemDTO> update(@PathVariable Long ruleSetId,
                                                 @PathVariable Long itemId,
                                                 @Valid @RequestBody AlertRuleItemUpdateRequestDTO req) {
    return ResponseEntity.ok(alertRuleAdminService.update(ruleSetId, itemId, req));
  }

  /** 删除（单个）—仅 DRAFT 可改 */
  @DeleteMapping("/{itemId}")
  public ResponseEntity<Void> delete(@PathVariable Long ruleSetId, @PathVariable Long itemId) {
    alertRuleAdminService.delete(ruleSetId, itemId);
    return ResponseEntity.noContent().build();
  }

  /** 批量删除—仅 DRAFT 可改 */
  @DeleteMapping
  public ResponseEntity<Integer> deleteBatch(@PathVariable Long ruleSetId,
                                             @Valid @RequestBody IdListRequestDTO req) {
    int n = alertRuleAdminService.deleteBatch(ruleSetId, req);
    return ResponseEntity.ok(n);
  }

  /** 批量启用/禁用—仅 DRAFT 可改 */
  @PostMapping("/enable")
  public ResponseEntity<Integer> enableBatch(@PathVariable Long ruleSetId,
                                             @Valid @RequestBody BatchEnableRequestDTO req) {
    int n = alertRuleAdminService.enableBatch(ruleSetId, req);
    return ResponseEntity.ok(n);
  }

}
