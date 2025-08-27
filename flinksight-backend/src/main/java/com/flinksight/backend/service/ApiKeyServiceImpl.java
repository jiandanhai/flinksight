package com.flinksight.backend.service;

import com.flinksight.backend.common.PageHelpers;
import com.flinksight.backend.domain.ApiKey;
import com.flinksight.backend.mapper.ApiKeyStructMapper;
import com.flinksight.backend.repository.ApiKeyRepository;
import com.flinksight.backend.security.SecurityUtil;
import com.flinksight.common.dto.ApiKeyDTO;
import com.flinksight.common.model.PageResult;
import com.flinksight.common.service.ApiKeyService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
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
    public PageResult<ApiKeyDTO> list(int page, int size) {
        PageRequest pr = PageHelpers.pageRequest(page, size, null, ApiKey.class); // 统一 1→0
        Page<ApiKey> result = repository.findByTenantIdAndIsDeleted(SecurityUtil.getCurrentTenantId(),0, pr);
        return PageHelpers.toPageResult(result, apiKeyStructMapper::toDTO, true); // 返回 1-ba
    }

    @Override
    public boolean sDelete(Long id) {
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
