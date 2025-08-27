package com.flinksight.backend.service;

import com.flinksight.backend.common.PageHelpers;
import com.flinksight.backend.domain.AlertHistory;
import com.flinksight.backend.mapper.AlertHistoryStructMapper;
import com.flinksight.backend.repository.AlertHistoryRepository;
import com.flinksight.backend.security.SecurityUtil;
import com.flinksight.common.dto.AlertHistoryDTO;
import com.flinksight.common.model.PageResult;
import com.flinksight.common.service.AlertHistoryService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
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
    public PageResult<AlertHistoryDTO> list(int page, int size) {
        PageRequest pr = PageHelpers.pageRequest(page, size, null, AlertHistory.class); // 统一 1→0
        Page<AlertHistory> result = repository.findByTenantIdAndIsDeleted(SecurityUtil.getCurrentTenantId(), 0,  pr);
        return PageHelpers.toPageResult(result, alertHistoryStructMapper::toDTO, true); // 返回 1-based
    }

    @Override
    public boolean sDelete(Long id) {
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
