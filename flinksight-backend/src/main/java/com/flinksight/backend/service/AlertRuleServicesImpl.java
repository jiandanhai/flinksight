// package com.flinksight.backend.service.impl
package com.flinksight.backend.service;

import com.flinksight.backend.domain.AlertNotifyPolicy;
import com.flinksight.backend.domain.AlertRuleItem;
import com.flinksight.backend.domain.AlertRuleSet;
import com.flinksight.backend.mapper.AlertRuleItemStructMapper;
import com.flinksight.backend.repository.AlertNotifyPolicyRepository;
import com.flinksight.backend.repository.AlertRuleItemRepository;
import com.flinksight.backend.repository.AlertRuleSetRepository;
import com.flinksight.backend.security.SecurityUtil;
import com.flinksight.common.dto.*;
import com.flinksight.common.enums.RuleSetStatus;
import com.flinksight.common.enums.ScopeType;
import com.flinksight.common.service.AlertRuleAdminService;
import com.flinksight.common.service.AlertRuleQueryService;
import com.flinksight.common.service.AlertRuleSetAdminService;
import com.flinksight.common.service.AlertRuleSetQueryService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.web.server.ResponseStatusException;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

import static org.springframework.http.HttpStatus.*;

@Service
@RequiredArgsConstructor
public class AlertRuleServicesImpl implements AlertRuleAdminService, AlertRuleQueryService, AlertRuleSetQueryService, AlertRuleSetAdminService {

  private final AlertRuleSetRepository setRepo;
  private final AlertRuleItemRepository itemRepo;
  private final AlertNotifyPolicyRepository policyRepo;
  private final AlertRuleItemStructMapper alertRuleItemStructMapper;


  // --------- Admin: 草稿 + 写入项 + 激活 ----------

  @Transactional
  @Override
  public Long createDraft(ScopeType scopeType, Long scopeId, Long version, String operator) {
    Long tenantId = SecurityUtil.getCurrentTenantId();
    if (setRepo.existsByTenantIdAndScopeTypeAndScopeIdAndVersion(tenantId, scopeType, scopeId, version)) {
      throw new IllegalStateException("版本已存在");
    }
    AlertRuleSet s = AlertRuleSet.builder()
        .tenantId(tenantId).scopeType(scopeType).scopeId(scopeId)
        .version(version).status(RuleSetStatus.DRAFT).activeFlag(0)
        .checksum(null).createdBy(operator).createdAt(LocalDateTime.now())
        .build();
    return setRepo.saveAndFlush(s).getId();
  }


  @Transactional
  @Override
  public void replaceItems(Long ruleSetId, List<AlertRuleItemDTO> items) {
    AlertRuleSet s = setRepo.findById(ruleSetId).orElseThrow();
    if (s.getStatus() == RuleSetStatus.ACTIVE) {
      throw new IllegalStateException("已激活版本不可修改");
    }
    itemRepo.deleteByRuleSetId(ruleSetId);
    if (!CollectionUtils.isEmpty(items)) {
      List<AlertRuleItem> arItems = new ArrayList<>();
      LocalDateTime now = LocalDateTime.now();
      for (AlertRuleItemDTO it : items) {
        AlertRuleItem entity = alertRuleItemStructMapper.toEntity(it);
        entity.setId(null);
        entity.setRuleSetId(ruleSetId);
        arItems.add(entity);
        if (it.getCreatedAt() == null) it.setCreatedAt(now);
      }

      itemRepo.saveAll(arItems);
    }
    // 预计算 checksum，便于激活/推送
    setChecksum(ruleSetId);
  }

  @Transactional
  @Override
  public void activate(Long ruleSetId) {
    AlertRuleSet s = setRepo.findById(ruleSetId).orElseThrow();
    // 同作用域原子切换
    setRepo.deactivateActive(s.getTenantId(), s.getScopeType(), s.getScopeId());
    s.setStatus(RuleSetStatus.ACTIVE);
    s.setActiveFlag(1);
    if (s.getChecksum() == null) setChecksum(ruleSetId);
    setRepo.saveAndFlush(s);
    // TODO: 可在此发布 Kafka 事件（配置信道），推送给采集/计算作业
  }

  private void setChecksum(Long ruleSetId) {
    List<AlertRuleItem> list = itemRepo.findByRuleSetId(ruleSetId);
    String joined = list.stream()
        .sorted(Comparator.comparing(AlertRuleItem::getMetricKey)
            .thenComparing(AlertRuleItem::getSeverity)
            .thenComparing(AlertRuleItem::getAggregator)
            .thenComparing(AlertRuleItem::getComparator)
            .thenComparing(AlertRuleItem::getWindowSeconds))
        .map(it -> String.join("|",
            it.getMetricKey(),
            it.getComparator().name(),
            String.valueOf(it.getThreshold()),
            String.valueOf(it.getWindowSeconds()),
            it.getAggregator().name(),
            it.getSeverity().name(),
            String.valueOf(it.getDedupMs()),
            String.valueOf(it.getAutoRecover()),
            String.valueOf(Optional.ofNullable(it.getNotifyPolicyId()).orElse(0L)),
            String.valueOf(it.getEnabled())
        ))
        .collect(Collectors.joining(";"));
    String sum = sha256(joined);
    AlertRuleSet s = setRepo.findById(ruleSetId).orElseThrow();
    s.setChecksum(sum);
    setRepo.save(s);
  }

  // --------- Query: 导出快照（作业侧拉取），支持分级覆盖 ----------

  @Override
  public RuleSnapshotDTO snapshot(ScopeType scopeType, Long scopeId) {
    // 1) 三层 ACTIVE：TENANT(NULL) < CLUSTER(id) < JOB(id)
    Map<String, Object> base = layer(ScopeType.TENANT, null);
    Map<String, Object> cluster = scopeType == ScopeType.CLUSTER || scopeType == ScopeType.JOB
        ? layer(ScopeType.CLUSTER, scopeType==ScopeType.CLUSTER? scopeId : null) : null;
    Map<String, Object> job = scopeType == ScopeType.JOB ? layer(ScopeType.JOB, scopeId) : null;

    // 2) 组装/合并：后者覆盖前者（按 metric+comparator+aggregator+window 唯一）
    List<AlertRuleItem> merged = new ArrayList<>();
    String sum = null; Long ver = 0L;

    if (base != null) {
      merged.addAll((List<AlertRuleItem>) base.get("items"));
      sum = (String) base.get("checksum");
      ver = Math.max(ver, (Long) base.get("version"));
    }
    if (cluster != null) {
      merged = override(merged, (List<AlertRuleItem>) cluster.get("items"));
      sum = (String) cluster.get("checksum");
      ver = Math.max(ver, (Long) cluster.get("version"));
    }
    if (job != null) {
      merged = override(merged, (List<AlertRuleItem>) job.get("items"));
      sum = (String) job.get("checksum");
      ver = Math.max(ver, (Long) job.get("version"));
    }

    // 3) 转 DTO
    List<RuleSnapshotDTO.Item> items = new ArrayList<>(merged.size());
    for (AlertRuleItem it : merged) {
      String policyName = null;
      if (it.getNotifyPolicyId() != null) {
        policyName = policyRepo.findById(it.getNotifyPolicyId()).map(AlertNotifyPolicy::getName).orElse(null);
      }
      items.add(RuleSnapshotDTO.Item.builder()
          .metric(it.getMetricKey())
          .comparator(it.getComparator().name())
          .threshold(it.getThreshold())
          .windowSeconds(it.getWindowSeconds())
          .aggregator(it.getAggregator().name())
          .severity(it.getSeverity().name())
          .dedupMs(it.getDedupMs())
          .autoRecover(it.getAutoRecover() != null && it.getAutoRecover() == 1)
          .notifyPolicy(policyName)
          .build());
    }

    return RuleSnapshotDTO.builder()
        .tenantId(SecurityUtil.getCurrentTenantId())
        .scope(new RuleSnapshotDTO.Scope(scopeType.name(), scopeId))
        .version(ver)
        .etag(sum == null ? null : "W/\"sha256:" + sum + "\"")
        .rules(items)
        .build();
  }

  private Map<String, Object> layer(ScopeType type, Long sid) {
    return setRepo.findFirstByTenantIdAndScopeTypeAndScopeIdAndActiveFlag(SecurityUtil.getCurrentTenantId(), type, sid, 1)
        .map(s -> {
          List<AlertRuleItem> items = itemRepo.findByRuleSetId(s.getId()).stream()
              .filter(it -> it.getEnabled() != null && it.getEnabled() == 1)
              .toList();
          Map<String, Object> m = new HashMap<>();
          m.put("version", s.getVersion());
          m.put("checksum", s.getChecksum());
          m.put("items", items);
          return m;
        }).orElse(null);
  }

  private List<AlertRuleItem> override(List<AlertRuleItem> base, List<AlertRuleItem> add) {
    if (add == null || add.isEmpty()) return base;
    Map<String, AlertRuleItem> map = new LinkedHashMap<>();
    for (AlertRuleItem it : base) map.put(key(it), it);
    for (AlertRuleItem it : add)  map.put(key(it), it); // 覆盖
    return new ArrayList<>(map.values());
  }

  private String key(AlertRuleItem it) {
    return it.getMetricKey()+"|"+it.getComparator().name()+"|"+it.getAggregator().name()+"|"+it.getWindowSeconds();
  }



  @Override
  public Page<AlertRuleItemDTO> list(Long ruleSetId, Integer enabled, Pageable pageable) {
    ensureRuleSetExists(ruleSetId);
    Page<AlertRuleItem> page = (enabled == null)
            ? itemRepo.findByRuleSetId(ruleSetId, pageable)
            : itemRepo.findByRuleSetIdAndEnabled(ruleSetId, enabled, pageable);
    return page.map(alertRuleItemStructMapper::toDTO);
  }

  @Override
  public AlertRuleItemDTO get(Long ruleSetId, Long itemId) {
    ensureRuleSetExists(ruleSetId);
    AlertRuleItem item = itemRepo.findByIdAndRuleSetId(itemId, ruleSetId)
            .orElseThrow(() -> new ResponseStatusException(NOT_FOUND, "规则不存在"));
    return alertRuleItemStructMapper.toDTO(item);
  }

  // ---------- 修改（仅 DRAFT 可改） ----------

  @Transactional
  @Override
  public AlertRuleItemDTO update(Long ruleSetId, Long itemId, AlertRuleItemUpdateRequestDTO req) {
    AlertRuleSet s = ensureRuleSetExists(ruleSetId);
    ensureDraft(s);

    AlertRuleItem item = itemRepo.findByIdAndRuleSetId(itemId, ruleSetId)
            .orElseThrow(() -> new ResponseStatusException(NOT_FOUND, "规则不存在"));

    item.setMetricKey(req.getMetricKey());
    item.setComparator(enumSafe(req.getComparator(), com.flinksight.common.enums.ComparatorOp.class));
    item.setThreshold(req.getThreshold());
    item.setWindowSeconds(req.getWindowSeconds());
    item.setAggregator(enumSafe(req.getAggregator(), com.flinksight.common.enums.Aggregator.class));
    item.setSeverity(enumSafe(req.getSeverity(), com.flinksight.common.enums.Severity.class));
    item.setDedupMs(req.getDedupMs());
    item.setAutoRecover(Boolean.TRUE.equals(req.getAutoRecover()) ? 1 : 0);
    item.setEnabled(req.getEnabled());
    item.setCreatedAt(item.getCreatedAt() == null ? LocalDateTime.now() : item.getCreatedAt());

    // 映射通知策略名 -> id（可为空）
    if (req.getNotifyPolicyName() != null && !req.getNotifyPolicyName().isBlank()) {
      Long policyId = policyRepo.findByTenantIdAndName(s.getTenantId(), req.getNotifyPolicyName())
              .map(AlertNotifyPolicy::getId)
              .orElseThrow(() -> new ResponseStatusException(BAD_REQUEST, "通知策略不存在: " + req.getNotifyPolicyName()));
      item.setNotifyPolicyId(policyId);
    } else {
      item.setNotifyPolicyId(null);
    }

    itemRepo.save(item);
    recomputeChecksum(ruleSetId);
    return alertRuleItemStructMapper.toDTO(item);
  }

  // ---------- 删除（仅 DRAFT 可改） ----------

  @Transactional
  @Override
  public void delete(Long ruleSetId, Long itemId) {
    AlertRuleSet s = ensureRuleSetExists(ruleSetId);
    ensureDraft(s);
    int n = itemRepo.softDeleteByRuleSetIdAndIdIn(ruleSetId, List.of(itemId));
    if (n == 0) throw new ResponseStatusException(NOT_FOUND, "规则不存在或已删除");
    recomputeChecksum(ruleSetId);
  }

  @Transactional
  @Override
  public int deleteBatch(Long ruleSetId, IdListRequestDTO ids) {
    AlertRuleSet s = ensureRuleSetExists(ruleSetId);
    ensureDraft(s);
    int affected = itemRepo.softDeleteByRuleSetIdAndIdIn(ruleSetId, ids.getIds());
    recomputeChecksum(ruleSetId);
    return affected;
  }

  // ---------- 启用/禁用（仅 DRAFT 可改） ----------

  @Transactional
  @Override
  public int enableBatch(Long ruleSetId, BatchEnableRequestDTO req) {
    AlertRuleSet s = ensureRuleSetExists(ruleSetId);
    ensureDraft(s);

    if (req.getIds() == null || req.getIds().isEmpty()) return 0;
    int affected = itemRepo.updateEnabledByIds(ruleSetId, req.getIds(), req.getEnabled());
    recomputeChecksum(ruleSetId);
    return affected;
  }


  // ---------- 查询：版本列表 ----------

  @Override
  public Page<RuleSetSummaryDTO> listVersions(Long tenantId, ScopeType scopeType, Long scopeId,
                                              RuleSetStatus status, Integer activeFlag,
                                              Pageable pageable) {
    Page<AlertRuleSet> page = setRepo.search(tenantId, scopeType, scopeId, status, activeFlag, pageable);
    // 批量查条目数可优化为聚合；此处简化为逐条count
    return page.map(s -> RuleSetSummaryDTO.builder()
            .id(s.getId())
            .tenantId(s.getTenantId())
            .scopeType(s.getScopeType().name())
            .scopeId(s.getScopeId())
            .version(s.getVersion())
            .status(s.getStatus().name())
            .activeFlag(s.getActiveFlag())
            .checksum(s.getChecksum())
            .createdBy(s.getCreatedBy())
            .createdAt(s.getCreatedAt())
            .itemCount(itemRepo.countByRuleSetId(s.getId()))
            .build());
  }

  // ---------- 查询：版本详情 ----------

  @Override
  public RuleSetDetailDTO detail(Long ruleSetId) {
    AlertRuleSet s = setRepo.findById(ruleSetId)
            .orElseThrow(() -> new ResponseStatusException(NOT_FOUND, "规则集不存在"));

    // 拉取规则项并按稳定顺序返回
    List<AlertRuleItem> items = itemRepo.findByRuleSetId(s.getId()).stream()
            .sorted(Comparator
                    .comparing(AlertRuleItem::getMetricKey, java.util.Comparator.nullsFirst(String::compareTo))
                    .thenComparing(it -> it.getSeverity().name())
                    .thenComparing(it -> it.getAggregator().name())
                    .thenComparing(it -> it.getComparator().name())
                    .thenComparing(AlertRuleItem::getWindowSeconds))
            .collect(Collectors.toList());

    // 复用已有 AlertRuleItemService 的映射或在此就地映射
    List<AlertRuleItemDTO> dtoItems = items.stream()
            .map(it -> {
              String policyName = null;
              if (it.getNotifyPolicyId() != null) {
                policyName = policyRepo.findById(it.getNotifyPolicyId()).map(AlertNotifyPolicy::getName).orElse(null);
              }
              return AlertRuleItemDTO.builder()
                      .id(it.getId())
                      .ruleSetId(it.getRuleSetId())
                      .metricKey(it.getMetricKey())
                      .comparator(it.getComparator().name())
                      .threshold(it.getThreshold())
                      .windowSeconds(it.getWindowSeconds())
                      .aggregator(it.getAggregator().name())
                      .severity(it.getSeverity().name())
                      .dedupMs(it.getDedupMs())
                      .autoRecover(it.getAutoRecover())
                      .notifyPolicyName(policyName)
                      .enabled(it.getEnabled())
                      .build();
            }).toList();

    return RuleSetDetailDTO.builder()
            .id(s.getId())
            .tenantId(s.getTenantId())
            .scopeType(s.getScopeType().name())
            .scopeId(s.getScopeId())
            .version(s.getVersion())
            .status(s.getStatus().name())
            .activeFlag(s.getActiveFlag())
            .checksum(s.getChecksum())
            .createdBy(s.getCreatedBy())
            .createdAt(s.getCreatedAt())
            .items(dtoItems)
            .build();
  }

  // ---------- 管理：删除草稿 ----------

  @Transactional
  @Override
  public void deleteDraft(Long ruleSetId) {
    AlertRuleSet s = setRepo.findById(ruleSetId)
            .orElseThrow(() -> new ResponseStatusException(NOT_FOUND, "规则集不存在"));
    if (s.getStatus() != RuleSetStatus.DRAFT) {
      throw new ResponseStatusException(CONFLICT, "仅 DRAFT 版本允许删除");
    }
    // 规则项有 FK ON DELETE CASCADE；为保险也可显式删除
    itemRepo.deleteByRuleSetId(ruleSetId);
    setRepo.delete(s);
  }

  // ---------- 管理：归档（紧急下线） ----------

  @Transactional
  @Override
  public void archive(Long ruleSetId) {
    AlertRuleSet s = setRepo.findById(ruleSetId)
            .orElseThrow(() -> new ResponseStatusException(NOT_FOUND, "规则集不存在"));
    if (s.getStatus() != RuleSetStatus.ACTIVE) {
      throw new ResponseStatusException(CONFLICT, "仅 ACTIVE 版本允许归档");
    }
    // 下线当前 ACTIVE
    s.setActiveFlag(0);
    s.setStatus(RuleSetStatus.ARCHIVED);
    setRepo.saveAndFlush(s);
    // 可在此推送“配置下线”事件到 Kafka（可选）
  }

  // ---------- 管理：回滚到指定历史版本（实质就是激活它） ----------

  @Transactional
  @Override
  public void rollbackTo(Long ruleSetId) {
    AlertRuleSet s = setRepo.findById(ruleSetId)
            .orElseThrow(() -> new ResponseStatusException(NOT_FOUND, "规则集不存在"));
    // 允许从 ARCHIVED/DRAFT 回滚（DRAFT 回滚即直接激活为当前）
    // 先原子撤销同作用域 ACTIVE
    setRepo.deactivateActive(s.getTenantId(), s.getScopeType(), s.getScopeId());
    s.setStatus(RuleSetStatus.ACTIVE);
    s.setActiveFlag(1);
    // 若 checksum 为空（理论上不会），可重算；此处省略重算以减少抖动
    setRepo.saveAndFlush(s);
    // TODO: 推送“配置激活”事件到 Kafka（可选）
  }


  // ---------- helpers ----------

  private AlertRuleSet ensureRuleSetExists(Long ruleSetId) {
    return setRepo.findById(ruleSetId)
            .orElseThrow(() -> new ResponseStatusException(NOT_FOUND, "规则集不存在"));
  }

  private void ensureDraft(AlertRuleSet s) {
    if (s.getStatus() != RuleSetStatus.DRAFT) {
      throw new ResponseStatusException(CONFLICT, "仅 DRAFT 版本允许修改/删除/启用禁用，请创建草稿或回滚版本");
    }
  }

  private <E extends Enum<E>> E enumSafe(String v, Class<E> e) {
    try { return Enum.valueOf(e, v); }
    catch (Exception ex) { throw new ResponseStatusException(BAD_REQUEST, "非法枚举值: " + v); }
  }

  /**
   * 重算指定规则集(ruleSetId)的内容校验和：
   * 1) 仅参与计算的规则项：is_deleted=0（由@SQLRestriction保证）且 enabled=1；
   * 2) 稳定排序：metricKey → severity → aggregator → comparator → windowSeconds；
   * 3) 规范化拼接：各字段用 '|' 连接，记录间用 ';' 连接；threshold 以 (20,6) 规范化；
   * 4) SHA-256 计算摘要，写回 alert_rule_set.checksum。
   */
  private void recomputeChecksum(Long ruleSetId) {
    // 1) 拉取候选项（@SQLRestriction 已过滤 is_deleted=1），仅保留 enabled=1
    final List<AlertRuleItem> list = itemRepo.findByRuleSetId(ruleSetId).stream()
            .filter(it -> Objects.equals(it.getEnabled(), 1))
            .sorted(Comparator
                    .comparing(AlertRuleItem::getMetricKey, Comparator.nullsFirst(String::compareTo))
                    .thenComparing(it -> it.getSeverity().name())
                    .thenComparing(it -> it.getAggregator().name())
                    .thenComparing(it -> it.getComparator().name())
                    .thenComparing(AlertRuleItem::getWindowSeconds))
            .collect(Collectors.toList());

    // 2) 按稳定顺序规范化拼接
    final String payload = list.stream()
            .map(it -> String.join("|",
                    safe(it.getMetricKey()),
                    it.getComparator().name(),
                    dec(it.getThreshold()),                    // 20,6 规范化
                    String.valueOf(it.getWindowSeconds()),
                    it.getAggregator().name(),
                    it.getSeverity().name(),
                    String.valueOf(it.getDedupMs()),
                    String.valueOf(it.getAutoRecover() == null ? 0 : it.getAutoRecover()),
                    String.valueOf(it.getNotifyPolicyId() == null ? 0L : it.getNotifyPolicyId())
            ))
            .collect(Collectors.joining(";"));

    // 3) 计算 SHA-256 指纹（空集合时也有确定性指纹）
    final String checksum = sha256(payload);

    // 4) 写回 rule_set
    final AlertRuleSet set = setRepo.findById(ruleSetId).orElseThrow();
    set.setChecksum(checksum);
    setRepo.save(set);
  }

  /** 将字符串安全化（null -> 空字符串，去除首尾空白）。*/
  private static String safe(String v) {
    return v == null ? "" : v.trim();
  }

  /** 规范化小数：与 DB DECIMAL(20,6) 对齐，去掉尾随 0，保持稳定文本表示。*/
  private static String dec(BigDecimal d) {
    if (d == null) return "0";
    return d
            .setScale(6, RoundingMode.HALF_UP)
            .stripTrailingZeros()
            .toPlainString();
  }


  private static String sha256(String s) {
    try {
      MessageDigest md = MessageDigest.getInstance("SHA-256");
      byte[] b = md.digest(s.getBytes(StandardCharsets.UTF_8));
      StringBuilder sb = new StringBuilder(64);
      for (byte x : b) sb.append(String.format("%02x", x));
      return sb.toString();
    } catch (Exception e) { return null; }
  }

}
