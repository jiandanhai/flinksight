package com.flinksight.backend.service;

import com.flinksight.backend.common.PageHelpers;
import com.flinksight.backend.domain.AlertRule;
import com.flinksight.backend.mapper.AlertRuleStructMapper;
import com.flinksight.backend.repository.AlertRuleRepository;
import com.flinksight.backend.security.SecurityUtil;
import com.flinksight.backend.security.tenant.TenantContext;
import com.flinksight.backend.security.tenant.TenantRequired;
import com.flinksight.common.dto.AlertRuleDTO;
import com.flinksight.common.dto.RuleMatchResultDTO;
import com.flinksight.common.dto.RuleTestRequestDTO;
import com.flinksight.common.model.PageResult;
import com.flinksight.common.service.AlertRuleService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Comparator;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * 报警规则业务实现
 * AlertRule Service Impl
 */
@Service
@RequiredArgsConstructor
@Transactional
@TenantRequired
public class AlertRuleServiceImpl implements AlertRuleService{

    private final AlertRuleRepository alertRuleRepository;
    private final AlertRuleStructMapper alertRuleStructMapper;

    @Override
    @Transactional(readOnly = true)
    public AlertRuleDTO createAlertRule(AlertRuleDTO alertRuleDTO) {
        AlertRule entity = alertRuleStructMapper.toEntity(alertRuleDTO);
        AlertRule saved = alertRuleRepository.save(entity);
        entity.setIsDeleted(0);
        return alertRuleStructMapper.toDTO(saved);
    }

    @Override
    @Transactional(readOnly = true)
    public AlertRuleDTO getAlertRule(Long id) {
        AlertRule r = alertRuleRepository.findByIdAndTenantIdAndIsDeleted(id, SecurityUtil.getCurrentTenantId(), 0)
                .orElseThrow(() -> new NoSuchElementException("规则不存在或不属于当前租户"));
        return alertRuleStructMapper.toDTO(r);
    }

    @Override
    @Transactional
    public PageResult<AlertRuleDTO> list(Long clusterId, Integer enable,int page, int size) {
        PageRequest pr = PageHelpers.pageRequest(page, size, null, AlertRule.class); // 统一 1→0
        Page<AlertRule> result =
                (enable == null)
                        // 不按启用状态筛选
                        ? (clusterId == null
                        ? alertRuleRepository.findAllByTenantIdAndIsDeleted(SecurityUtil.getCurrentTenantId(), 0, pr)
                        : alertRuleRepository.findAllByTenantIdAndClusterIdAndIsDeleted(SecurityUtil.getCurrentTenantId(), clusterId, 0, pr))
                        // 按启用状态筛选
                        : (clusterId == null
                        ? alertRuleRepository.findAllByTenantIdAndEnableAndIsDeleted(SecurityUtil.getCurrentTenantId(), enable, 0, pr)
                        : alertRuleRepository.findAllByTenantIdAndClusterIdAndEnableAndIsDeleted(SecurityUtil.getCurrentTenantId(), clusterId, enable, 0, pr));
        return PageHelpers.toPageResult(result, alertRuleStructMapper::toDTO, true); // 返回 1-based
    }

    @Override
    @Transactional
    public AlertRuleDTO updateAlertRule(Long id, AlertRuleDTO patch) {
        AlertRule r = alertRuleRepository.findByIdAndTenantIdAndIsDeleted(id, SecurityUtil.getCurrentTenantId(), 0)
                .orElseThrow(() -> new NoSuchElementException("规则不存在或不属于当前租户"));
        alertRuleStructMapper.mergeIgnoreNullAndBlank(patch, r);
        AlertRule saved = alertRuleRepository.save(r);
        return alertRuleStructMapper.toDTO(saved);
    }

    @Override
    @Transactional
    public boolean sDelete(Long id) {
        AlertRule r = alertRuleRepository.findByIdAndTenantIdAndIsDeleted(id, SecurityUtil.getCurrentTenantId(), 0)
                .orElseThrow(() -> new NoSuchElementException("规则不存在或不属于当前租户"));
        r.setIsDeleted(1);
        alertRuleRepository.save(r);
        return true;
    }

    @Override
    public void deleteAlertRules(List<Long> ids) {
        for (Long id : ids) sDelete(id);
    }



    @Override
    @Transactional
    public AlertRuleDTO setEnable(Long id, boolean enable) {
        Long tenantId = TenantContext.getTenantId();
        AlertRule r = alertRuleRepository.findByIdAndTenantIdAndIsDeleted(id, tenantId, 0)
                .orElseThrow(() -> new NoSuchElementException("规则不存在或不属于当前租户"));
        r.setEnable(enable ? 1 : 0);
        r.setCreatedAt(r.getCreatedAt()); // 保留
        return alertRuleStructMapper.toDTO(alertRuleRepository.save(r));
    }

    @Override
    @Transactional
    public void setEnableBatch(List<Long> ids, boolean enable) {
        for (Long id : ids) setEnable(id, enable);
    }

    @Override
    @Transactional(readOnly = true)
    public List<RuleMatchResultDTO> testMatch(RuleTestRequestDTO req) {
        // 1) 归一化：metric/op/threshold 支持从 type/message 兜底解析
        String metric = req.getMetricKey();
        if (metric == null && req.getType() != null) {
            metric = toMetricKey(req.getType());
        }
        String op = req.getCompareOp();
        if (op == null && req.getMessage() != null) {
            op = parseCompareOp(req.getMessage());
        }
        Double th = req.getThreshold();
        if (th == null && req.getMessage() != null) {
            th = parseThreshold(req.getMessage());
        }
        final String fMetric = metric == null ? null : metric.trim();
        final String fOp     = op == null ? null : op.trim();
        final Double fTh     = th;

        // 2) 拉规则（只看当前租户 / 未删除）
        Long tenantId = SecurityUtil.getCurrentTenantId();
        List<AlertRule> all = alertRuleRepository
                .findByTenantIdAndIsDeleted(tenantId, 0);

        // 3) 评分 + 排序（分高在前）
        Comparator<AlertRule> byScoreDesc = (a, b) -> {
            int sb = score(b, req.getClusterId(), fMetric, fOp, fTh);
            int sa = score(a, req.getClusterId(), fMetric, fOp, fTh);
            return Integer.compare(sb, sa);
        };

        return all.stream()
                .sorted(byScoreDesc)
                .map(r -> {
                    RuleMatchResultDTO vo = new RuleMatchResultDTO();
                    vo.setRule(alertRuleStructMapper.toDTO(r));
                    int s = score(r, req.getClusterId(), fMetric, fOp, fTh);
                    vo.setScore(s);
                    vo.setMatchedCluster(
                            req.getClusterId() != null &&
                                    Objects.equals(r.getClusterId(), req.getClusterId()));
                    vo.setMatchedMetric(fMetric != null &&
                            fMetric.equalsIgnoreCase(r.getMetricKey()));
                    vo.setMatchedOp(fOp != null &&
                            fOp.equalsIgnoreCase(r.getCompareOp()));
                    if (fTh != null && r.getThreshold() != null) {
                        vo.setThresholdDiff(Math.abs(r.getThreshold() - fTh));
                    }
                    return vo;
                })
                .collect(Collectors.toList());
    }


    // ====== 与 AlertServiceImpl 使用的解析/打分保持一致（复制一份，避免跨服务耦合） ======

    private String toMetricKey(String type) {
        if (type == null) return null;
        switch (type.toLowerCase()) {
            case "cpuhigh":  return "cpu";
            case "memhigh":  return "mem";
            case "diskhigh": return "disk";
            case "jobfailed":return "job";
            default:         return null;
        }
    }

    private String parseCompareOp(String message) {
        if (message == null) return null;
        var m = message.replaceAll("\\s+", "");
        var op = m.replaceAll(".*([><=]{1,2}).*", "$1");
        return (op.equals(m)) ? null : op;
    }

    private Double parseThreshold(String message) {
        if (message == null) return null;
        String num = message.replaceAll(".*?([0-9]+(\\.[0-9]+)?).*", "$1");
        if (num.equals(message)) return null;
        Double v = Double.valueOf(num);
        if (message.contains("%")) return v / 100d;
        return v;
    }

    /** 规则匹配“打分”：同集群+20、指标一致+10、比较符一致+5、阈值误差小+0~3 */
    private int score(AlertRule r, Long clusterId, String metric, String op, Double th) {
        int score = 0;
        if (clusterId != null && Objects.equals(r.getClusterId(), clusterId)) score += 20;
        if (metric != null && metric.equalsIgnoreCase(r.getMetricKey())) score += 10;
        if (op != null && op.equals(r.getCompareOp())) score += 5;
        if (th != null && r.getThreshold() != null) {
            double diff = Math.abs(r.getThreshold() - th);
            if (diff < 0.000001) score += 3;
            else if (diff < 0.05) score += 2;
            else if (diff < 0.1) score += 1;
        }
        return score;
    }
}
