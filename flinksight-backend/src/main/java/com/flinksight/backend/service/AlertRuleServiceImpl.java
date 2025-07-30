package com.flinksight.backend.service;

import com.flinksight.backend.domain.AlertHistory;
import com.flinksight.backend.domain.AlertRule;
import com.flinksight.backend.exception.BusinessException;
import com.flinksight.backend.mapper.AlertRuleStructMapper;
import com.flinksight.backend.repository.AlertRuleRepository;
import com.flinksight.backend.security.tenant.TenantRequired;
import com.flinksight.common.dto.AlertHistoryDTO;
import com.flinksight.common.dto.AlertRuleDTO;
import com.flinksight.common.service.AlertRuleService;
import com.flinksight.common.enums.ErrorCode;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

/**
 * 报警规则业务实现
 * AlertRule Service Impl
 */
@Service
@RequiredArgsConstructor
@Transactional
@TenantRequired
public class AlertRuleServiceImpl implements AlertRuleService{

    private final AlertRuleRepository repository;
    private final AlertRuleStructMapper mapper;

    @Override
    public AlertRuleDTO createRule(AlertRuleDTO alertRuleDTO) {
        AlertRule entity = mapper.toEntity(alertRuleDTO);
        AlertRule saved = repository.save(entity);
        entity.setIsDeleted(0);
        return mapper.toDTO(saved);
    }

    @Override
    public Optional<AlertRuleDTO> getRuleById(Long id) {
        return repository.findById(id).map(mapper::toDTO).filter(e -> e.getIsDeleted() == 0);
    }

    @Override
    public List<AlertRuleDTO> getRulesByTenant(Long tenantId) {
        return mapper.toDTOList(repository.findByTenantIdAndEnableAndIsDeleted(tenantId,1,0));
    }

    @Override
    public AlertRuleDTO updateRule(AlertRuleDTO rule) {
        Optional<AlertRuleDTO> opt = repository.findById(rule.getId()).map(mapper::toDTO);

        if(opt.isPresent()) {
            AlertRuleDTO r = opt.get();
            r.setMetricKey(rule.getMetricKey());
            r.setThreshold(rule.getThreshold());
            r.setCompareOp(rule.getCompareOp());
            r.setChannel(rule.getChannel());
            r.setEnable(rule.getEnable());
            // 其它字段
            AlertRule entity = mapper.toEntity(r);
            AlertRule saved = repository.save(entity);
            return mapper.toDTO(saved);
        }
        throw new BusinessException(ErrorCode.NOT_FOUND, "报警规则不存在");
    }

    @Override
    public boolean softDelete(Long id) {
        Optional<AlertRuleDTO> opt = repository.findById(id).map(mapper::toDTO).filter(e -> e.getIsDeleted() == 0);
        if (opt.isPresent()) {
            AlertRuleDTO dto = opt.get();
            dto.setIsDeleted(1);
            AlertRule entity = mapper.toEntity(dto);
            repository.save(entity);
            return true;
        }
        return false;
    }
}
