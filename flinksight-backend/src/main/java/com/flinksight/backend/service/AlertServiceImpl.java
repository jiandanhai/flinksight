package com.flinksight.backend.service;

import com.flinksight.backend.domain.Alert;
import com.flinksight.backend.exception.BusinessException;
import com.flinksight.backend.mapper.AlertStructMapper;
import com.flinksight.backend.repository.AlertRepository;
import com.flinksight.backend.security.tenant.TenantRequired;
import com.flinksight.common.dto.AlertDTO;
import com.flinksight.common.enums.ErrorCode;
import com.flinksight.common.model.PageResult;
import com.flinksight.common.service.AlertService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.Optional;

/**
 * 报警事件业务实现
 * Alert Service Impl
 */
@Service
@RequiredArgsConstructor
@Transactional
@TenantRequired
public class AlertServiceImpl implements AlertService{

    private final AlertRepository repository;
    private final AlertStructMapper alertStructMapper;

    @Override
    public AlertDTO createAlert(AlertDTO alertDTO) {
        Alert entity = alertStructMapper.toEntity(alertDTO);
        entity.setIsDeleted(0);
        Alert saved = repository.save(entity);
        return alertStructMapper.toDTO(saved);
    }

    @Override
    public Optional<AlertDTO> getAlertById(Long id) {
        return repository.findById(id).map(alertStructMapper::toDTO).filter(e -> e.getIsDeleted() == 0);
    }

    @Override
    public PageResult<AlertDTO> getAlertsByTenantAndStatus(Long tenantId, Integer status,int page, int size) {
        Page<Alert> result = repository.findByTenantIdAndStatusAndIsDeleted(tenantId, 1,0, PageRequest.of(page, size, Sort.by("id").descending()));
        Page<AlertDTO> dtoPage = result.map(alertStructMapper::toDTO);
        return new PageResult<>(dtoPage);
    }

    @Override
    public PageResult<AlertDTO> getAlertsByJobAndStatus(Long jobId, Integer status,int page, int size) {
        Page<Alert> result = repository.findByJobIdAndStatusAndIsDeleted(jobId, status,0, PageRequest.of(page, size, Sort.by("id").descending()));
        Page<AlertDTO> dtoPage = result.map(alertStructMapper::toDTO);
        return new PageResult<>(dtoPage);
    }

    @Override
    public AlertDTO updateAlert(AlertDTO alertDTO) {
        Optional<AlertDTO> opt = repository.findById(alertDTO.getId()).map(alertStructMapper::toDTO);
        if(opt.isPresent()) {
            AlertDTO a = opt.get();
            a.setLevel(alertDTO.getLevel());
            a.setType(alertDTO.getType());
            a.setMessage(alertDTO.getMessage());
            a.setStatus(alertDTO.getStatus());
            a.setHandlerId(alertDTO.getHandlerId());
            a.setUpdateTime(alertDTO.getUpdateTime());
            // 其它业务字段
            Alert entity = alertStructMapper.toEntity(a);
            Alert saved = repository.save(entity);
            return alertStructMapper.toDTO(saved);
        }
        throw new BusinessException(ErrorCode.NOT_FOUND, "报警事件不存在");
    }

    @Override
    public boolean softDelete(Long id) {
        Optional<AlertDTO> opt = repository.findById(id).map(alertStructMapper::toDTO).filter(e -> e.getIsDeleted() == 0);
        if (opt.isPresent()) {
            AlertDTO dto = opt.get();
            dto.setIsDeleted(1);
            Alert entity = alertStructMapper.toEntity(dto);
            repository.save(entity);
            return true;
        }
        return false;
    }
}
