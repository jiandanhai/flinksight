package com.flinksight.backend.service;

import com.flinksight.backend.domain.AlertHistory;
import com.flinksight.backend.mapper.AlertHistoryStructMapper;
import com.flinksight.backend.repository.AlertHistoryRepository;
import com.flinksight.common.dto.AlertHistoryDTO;
import com.flinksight.common.model.PageResult;
import com.flinksight.common.service.AlertHistoryService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional
public class AlertHistoryServiceImpl implements AlertHistoryService {
    private final AlertHistoryRepository repository;
    private final AlertHistoryStructMapper alertHistoryStructMapper;

    @Override
    public AlertHistoryDTO createOrUpdate(AlertHistoryDTO alertHistoryDTO) {
        AlertHistory entity = alertHistoryStructMapper.toEntity(alertHistoryDTO);
        entity.setIsDeleted(0);
        return alertHistoryStructMapper.toDTO(repository.save(entity));
    }

    @Override
    public Optional<AlertHistoryDTO> getById(Long id) {
        return repository.findById(id).map(alertHistoryStructMapper::toDTO).filter(e -> e.getIsDeleted() == 0);
    }

    @Override
    public PageResult<AlertHistoryDTO> findByTenantId(Long tenantId,int page, int size) {
        Page<AlertHistory> result = repository.findByTenantIdAndIsDeleted(tenantId, 0,  PageRequest.of(page, size, Sort.by("id").descending()));
        Page<AlertHistoryDTO> dtoPage = result.map(alertHistoryStructMapper::toDTO);
        return new PageResult<>(dtoPage);
    }

    @Override
    public PageResult<AlertHistoryDTO> getAll(int page, int size) {
        Page<AlertHistory> result = repository.findByIsDeleted(0, PageRequest.of(page, size, Sort.by("id").descending()));
        Page<AlertHistoryDTO> dtoPage = result.map(alertHistoryStructMapper::toDTO);
        return new PageResult<>(dtoPage);
    }

    @Override
    public boolean softDelete(Long id) {
        Optional<AlertHistoryDTO> opt = repository.findById(id).map(alertHistoryStructMapper::toDTO).filter(e -> e.getIsDeleted() == 0);
        if (opt.isPresent()) {
            AlertHistoryDTO dto = opt.get();
            dto.setIsDeleted(1);
            AlertHistory entity = alertHistoryStructMapper.toEntity(dto);
            repository.save(entity);
            return true;
        }
        return false;
    }
}
