package com.flinksight.backend.service;

import com.flinksight.backend.common.PageHelpers;
import com.flinksight.backend.domain.Alert;
import com.flinksight.backend.domain.AlertHistory;
import com.flinksight.backend.domain.AlertRule;
import com.flinksight.backend.exception.BusinessException;
import com.flinksight.backend.mapper.AlertHistoryStructMapper;
import com.flinksight.backend.mapper.AlertRuleStructMapper;
import com.flinksight.backend.mapper.AlertStructMapper;
import com.flinksight.backend.repository.AlertHistoryRepository;
import com.flinksight.backend.repository.AlertRepository;
import com.flinksight.backend.repository.AlertRuleRepository;
import com.flinksight.backend.security.SecurityUtil;
import com.flinksight.backend.security.tenant.TenantRequired;
import com.flinksight.common.dto.AlertDTO;
import com.flinksight.common.dto.AlertHistoryDTO;
import com.flinksight.common.dto.AlertOpsDTO;
import com.flinksight.common.dto.AlertRuleDTO;
import com.flinksight.common.enums.AlertLevelEnum;
import com.flinksight.common.enums.ErrorCode;
import com.flinksight.common.model.PageResult;
import com.flinksight.common.service.AlertService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.OutputStream;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

/**
 * 报警事件业务实现
 * Alert Service Impl
 */
@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
@TenantRequired
public class AlertServiceImpl implements AlertService{

    private final AlertRepository alertRepository;
    private final AlertRuleRepository alertRuleRepository;
    private final AlertHistoryRepository alertHistoryRepository;

    private final AlertStructMapper alertStructMapper;
    private final AlertRuleStructMapper alertRuleStructMapper;
    private final AlertHistoryStructMapper alertHistoryStructMapper;

    @Override
    @Transactional(readOnly = true)
    public AlertDTO getAlert(Long id) {
        Alert entity = alertRepository.findById(id)
                .filter(a -> Objects.equals(a.getTenantId(), SecurityUtil.getCurrentTenantId()) && a.getIsDeleted() == 0)
                .orElseThrow(() -> new NoSuchElementException("告警不存在或不属于当前租户"));
        return alertStructMapper.toDTO(entity);
    }

    @Override
    @Transactional(readOnly = true)
    public List<AlertRuleDTO> getAlertRules(Long alertId) {
        AlertDTO alert = getAlert(alertId);

        // 先拿当前租户启用/未删的规则
        List<AlertRule> rules = alertRuleRepository.findByTenantIdAndIsDeleted(SecurityUtil.getCurrentTenantId(), 0);

        // 规则筛选（宽松匹配：集群优先匹配、指标/比较符/阈值能对上的排前）
        String metric = toMetricKey(alert.getType());
        String op = parseCompareOp(alert.getMessage());
        Double th = parseThreshold(alert.getMessage());

        return rules.stream()
                .filter(r -> Objects.equals(r.getEnable(), 1))
                .sorted((r1, r2) -> {
                    int score1 = scoreRuleMatch(r1, alert.getClusterId(), metric, op, th);
                    int score2 = scoreRuleMatch(r2, alert.getClusterId(), metric, op, th);
                    return Integer.compare(score2, score1); // 分高在前
                })
                .map(alertRuleStructMapper::toDTO)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<AlertHistoryDTO> getAlertHistory(Long alertId) {
        List<AlertHistory> list = alertHistoryRepository
                .findByAlertIdAndTenantIdAndIsDeletedOrderByCreatedAtDesc(alertId, SecurityUtil.getCurrentTenantId(), 0);
        return alertHistoryStructMapper.toDTOList(list);
    }

    @Override
    @Transactional(readOnly = true)
    public AlertOpsDTO getAlertOps(Long alertId) {
        AlertDTO dto = getAlert(alertId);
        List<String> actions = new ArrayList<>();
        if (dto.getStatus() != null) {
            switch (dto.getStatus()) {
                case 0 -> actions = List.of("ACKNOWLEDGE", "ASSIGN", "CLOSE");
                case 1 -> actions = List.of("ASSIGN", "CLOSE");
                case 2 -> actions = List.of(); // 已关闭不可操作
                default -> actions = List.of();
            }
        }
        List<AlertHistoryDTO> last = getAlertHistory(alertId).stream().limit(5).collect(Collectors.toList());
        AlertOpsDTO vo = new AlertOpsDTO();
        vo.setAlertId(alertId);
        vo.setAllowedActions(actions);
        vo.setLastHistories(last);
        return vo;
    }


    @Override
    @Transactional(readOnly = true)
    public PageResult<AlertDTO> list(Long jobId, Long clusterId,String level, Integer status, int page, int size) {
        PageRequest pr = PageHelpers.pageRequest(page, size, null, Alert.class); // 统一 1→0
        log.info("[Service] about to call repo.pageQuery ,jobId={}， clusterId={}, level={}, status={}", jobId,clusterId, level, status);
        Page<Alert> result = alertRepository.pageQuery(SecurityUtil.getCurrentTenantId(), jobId,clusterId, level, status, pr);
        log.info("[Service] about to call repo.pageQuery ,result={}", result);
        return PageHelpers.toPageResult(result, alertStructMapper::toDTO, true); // 返回 1-based
    }

    @Override
    @Transactional(readOnly = true)
    public AlertDTO updateAlert(Long id,AlertDTO alertDTO) {
        Optional<AlertDTO> opt = alertRepository.findById(alertDTO.getId()).map(alertStructMapper::toDTO);
        if(opt.isPresent()) {
            AlertDTO a = opt.get();
            a.setLevel(alertDTO.getLevel());
            a.setType(alertDTO.getType());
            a.setMessage(alertDTO.getMessage());
            a.setStatus(alertDTO.getStatus());
            a.setHandlerId(alertDTO.getHandlerId());
            a.setUpdatedAt(alertDTO.getUpdatedAt());
            // 其它业务字段
            Alert entity = alertStructMapper.toEntity(a);
            Alert saved = alertRepository.save(entity);
            return alertStructMapper.toDTO(saved);
        }
        throw new BusinessException(ErrorCode.NOT_FOUND, "报警事件不存在");
    }

    @Override
    public void exportAlerts(String level, Integer status, OutputStream out) {

    }


    @Override
    @Transactional
    public boolean sDelete(Long id) {
        Alert a = alertRepository.findById(id)
                .filter(it -> Objects.equals(it.getTenantId(), SecurityUtil.getCurrentTenantId()) && it.getIsDeleted() == 0)
                .orElseThrow(() -> new NoSuchElementException("告警不存在或不属于当前租户"));
        a.setIsDeleted(1);
        a.setUpdatedAt(LocalDateTime.now());
        alertRepository.save(a);

        writeHistory(a.getId(), a.getTenantId(), "软删除告警", a.getStatus(), a.getLevel());
        return true;
    }


    @Override
    @Transactional
    public void deleteAlerts(List<Long> ids) {
        for (Long id : ids) sDelete(id);
    }

    // ---------- 私有工具 ----------

    private void writeHistory(Long alertId, Long tenantId, String content, Integer status, String level) {
        AlertHistory h = AlertHistory.builder()
                .alertId(alertId)
                .ruleId(null)
                .content(content)
                .level(AlertLevelEnum.ofCode(level).getCode())
                .status(status == null ? 0 : status)
                .operatorId(SecurityUtil.getCurrentUserId())
                .tenantId(tenantId)
                .operateTime(LocalDateTime.now())
                .isDeleted(0)
                .createdAt(LocalDateTime.now())
                .build();
        alertHistoryRepository.save(h);
    }

    private String toMetricKey(String type) {
        if (type == null) return null;
        switch (type.toLowerCase()) {
            case "cpuhigh": return "cpu";
            case "memhigh": return "mem";
            case "diskhigh": return "disk";
            case "jobfailed": return "job";
            default: return null;
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
    private int scoreRuleMatch(AlertRule r, Long clusterId, String metric, String op, Double th) {
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
