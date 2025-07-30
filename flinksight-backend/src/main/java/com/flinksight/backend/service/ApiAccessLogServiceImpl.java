package com.flinksight.backend.service;

import com.flinksight.backend.domain.Alert;
import com.flinksight.backend.domain.ApiAccessLog;
import com.flinksight.backend.mapper.AlertStructMapper;
import com.flinksight.backend.mapper.ApiAccessLogStructMapper;
import com.flinksight.backend.repository.ApiAccessLogRepository;
import com.flinksight.common.dto.AlertDTO;
import com.flinksight.common.dto.ApiAccessLogDTO;
import com.flinksight.common.service.ApiAccessLogService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional
public class ApiAccessLogServiceImpl implements ApiAccessLogService {
    private final ApiAccessLogRepository repository;
    private final ApiAccessLogStructMapper mapper;

    @Override
    public ApiAccessLogDTO createOrUpdate(ApiAccessLogDTO apiAccessLogDTO) {
        ApiAccessLog entity = mapper.toEntity(apiAccessLogDTO);
        ApiAccessLog saved = repository.save(entity);
        entity.setIsDeleted(0);
        return mapper.toDTO(saved);
    }

    @Override
    public Optional<ApiAccessLogDTO> getById(Long id) {
        return repository.findById(id).map(mapper::toDTO).filter(e -> e.getIsDeleted() == 0);
    }

    @Override
    public List<ApiAccessLogDTO> findByTenantId(Long tenantId) {
        return mapper.toDTOList(repository.findByTenantIdAndIsDeleted(tenantId,0));
    }

    @Override
    public List<ApiAccessLogDTO> getAll() {
        return mapper.toDTOList(repository.findAll().stream().filter(e -> e.getIsDeleted() == 0).toList());
    }

    @Override
    public boolean softDelete(Long id) {
        Optional<ApiAccessLogDTO> opt = repository.findById(id).map(mapper::toDTO).filter(e -> e.getIsDeleted() == 0);
        if (opt.isPresent()) {
            ApiAccessLogDTO dto = opt.get();
            dto.setIsDeleted(1);
            ApiAccessLog entity = mapper.toEntity(dto);
            repository.save(entity);
            return true;
        }
        return false;
    }
}
