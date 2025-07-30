package com.flinksight.backend.service;

import com.flinksight.backend.domain.AlertHistory;
import com.flinksight.backend.mapper.AlertHistoryStructMapper;
import com.flinksight.backend.repository.AlertHistoryRepository;
import com.flinksight.common.dto.AlertHistoryDTO;
import com.flinksight.common.service.AlertHistoryService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.List;
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
    public List<AlertHistoryDTO> findByTenantId(Long tenantId) {
        return alertHistoryStructMapper.toDTOList(repository.findByTenantIdAndIsDeleted(null,0));
    }

    @Override
    public List<AlertHistoryDTO> getAll() {
        return alertHistoryStructMapper.toDTOList(repository.findAll().stream().filter(e -> e.getIsDeleted() == 0).toList());
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
