package com.flinksight.backend.service;

import com.flinksight.backend.domain.ApiAccessLog;
import com.flinksight.backend.domain.ApiKey;
import com.flinksight.backend.mapper.ApiAccessLogStructMapper;
import com.flinksight.backend.mapper.ApiKeyStructMapper;
import com.flinksight.backend.repository.ApiKeyRepository;
import com.flinksight.common.dto.AlertHistoryDTO;
import com.flinksight.common.dto.ApiAccessLogDTO;
import com.flinksight.common.dto.ApiKeyDTO;
import com.flinksight.common.service.ApiKeyService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional
public class ApiKeyServiceImpl implements ApiKeyService {
    private final ApiKeyRepository repository;
    private final ApiKeyStructMapper mapper;

    @Override
    public ApiKeyDTO createOrUpdate(ApiKeyDTO apiKeyDTO) {
        ApiKey entity = mapper.toEntity(apiKeyDTO);
        ApiKey saved = repository.save(entity);
        entity.setIsDeleted(0);
        return mapper.toDTO(saved);
    }

    @Override
    public Optional<ApiKeyDTO> getById(Long id) {
        return repository.findById(id).map(mapper::toDTO).filter(e -> e.getIsDeleted() == 0);
    }

    @Override
    public List<ApiKeyDTO> findByTenantId(Long tenantId) {
        return mapper.toDTOList(repository.findByTenantIdAndIsDeleted(tenantId,0));
    }

    @Override
    public List<ApiKeyDTO> getAll() {
        return mapper.toDTOList(repository.findAll().stream().filter(e -> e.getIsDeleted() == 0).toList());
    }

    @Override
    public boolean softDelete(Long id) {
        Optional<ApiKeyDTO> opt = repository.findById(id).map(mapper::toDTO).filter(e -> e.getIsDeleted() == 0);
        if (opt.isPresent()) {
            ApiKeyDTO dto = opt.get();
            dto.setIsDeleted(1);
            ApiKey entity = mapper.toEntity(dto);
            repository.save(entity);
            return true;
        }
        return false;
    }
}
