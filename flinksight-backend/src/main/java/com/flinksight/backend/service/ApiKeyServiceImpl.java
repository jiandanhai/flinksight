package com.flinksight.backend.service;

import com.flinksight.backend.domain.ApiKey;
import com.flinksight.backend.mapper.ApiKeyStructMapper;
import com.flinksight.backend.repository.ApiKeyRepository;
import com.flinksight.common.dto.ApiKeyDTO;
import com.flinksight.common.model.PageResult;
import com.flinksight.common.service.ApiKeyService;
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
public class ApiKeyServiceImpl implements ApiKeyService {
    private final ApiKeyRepository repository;
    private final ApiKeyStructMapper apiKeyStructMapper;

    @Override
    public ApiKeyDTO createOrUpdate(ApiKeyDTO apiKeyDTO) {
        ApiKey entity = apiKeyStructMapper.toEntity(apiKeyDTO);
        ApiKey saved = repository.save(entity);
        entity.setIsDeleted(0);
        return apiKeyStructMapper.toDTO(saved);
    }

    @Override
    public Optional<ApiKeyDTO> getById(Long id) {
        return repository.findById(id).map(apiKeyStructMapper::toDTO).filter(e -> e.getIsDeleted() == 0);
    }

    @Override
    public PageResult<ApiKeyDTO> findByTenantId(Long tenantId,int page, int size) {
        Page<ApiKey> result = repository.findByTenantIdAndIsDeleted(tenantId,0, PageRequest.of(page, size, Sort.by("id").descending()));
        Page<ApiKeyDTO> dtoPage = result.map(apiKeyStructMapper::toDTO);
        return new PageResult<>(dtoPage);
    }

    @Override
    public PageResult<ApiKeyDTO> getAll(int page, int size) {
        Page<ApiKey> result = repository.findByIsDeleted(0, PageRequest.of(page, size, Sort.by("id").descending()));
        Page<ApiKeyDTO> dtoPage = result.map(apiKeyStructMapper::toDTO);
        return new PageResult<>(dtoPage);
    }

    @Override
    public boolean softDelete(Long id) {
        Optional<ApiKeyDTO> opt = repository.findById(id).map(apiKeyStructMapper::toDTO).filter(e -> e.getIsDeleted() == 0);
        if (opt.isPresent()) {
            ApiKeyDTO dto = opt.get();
            dto.setIsDeleted(1);
            ApiKey entity = apiKeyStructMapper.toEntity(dto);
            repository.save(entity);
            return true;
        }
        return false;
    }
}
