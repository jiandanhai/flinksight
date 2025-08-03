package com.flinksight.backend.service;

import com.flinksight.backend.domain.IntegrationConfig;
import com.flinksight.backend.mapper.IntegrationConfigStructMapper;
import com.flinksight.backend.repository.IntegrationConfigRepository;
import com.flinksight.common.dto.IntegrationConfigDTO;
import com.flinksight.common.model.PageResult;
import com.flinksight.common.service.IntegrationConfigService;
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
public class IntegrationConfigServiceImpl implements IntegrationConfigService {
    private final IntegrationConfigRepository repository;
    private final IntegrationConfigStructMapper integrationConfigStructMapper;

    @Override
    public IntegrationConfigDTO createOrUpdate(IntegrationConfigDTO integrationConfigDTO) {
        IntegrationConfig entity = integrationConfigStructMapper.toEntity(integrationConfigDTO);
        entity.setIsDeleted(0);
        return integrationConfigStructMapper.toDTO(repository.save(entity));
    }

    @Override
    public Optional<IntegrationConfigDTO> getById(Long id) {
        return repository.findById(id).map(integrationConfigStructMapper::toDTO).filter(e -> e.getIsDeleted() == 0);
    }

    @Override
    public PageResult<IntegrationConfigDTO> findByTenantId(Long tenantId,int page, int size) {
        Page<IntegrationConfig> result = repository.findByTenantIdAndIsDeleted(tenantId,0, PageRequest.of(page, size, Sort.by("id").descending()));
        Page<IntegrationConfigDTO> dtoPage = result.map(integrationConfigStructMapper::toDTO);
        return new PageResult<>(dtoPage);
    }

    @Override
    public PageResult<IntegrationConfigDTO> getAll(int page, int size) {
        Page<IntegrationConfig> result = repository.findByIsDeleted(0, PageRequest.of(page, size, Sort.by("id").descending()));
        Page<IntegrationConfigDTO> dtoPage = result.map(integrationConfigStructMapper::toDTO);
        return new PageResult<>(dtoPage);
    }

    @Override
    public boolean softDelete(Long id) {
        Optional<IntegrationConfigDTO> opt = repository.findById(id).map(integrationConfigStructMapper::toDTO).filter(e -> e.getIsDeleted() == 0);
        if (opt.isPresent()) {
            IntegrationConfigDTO dto = opt.get();
            dto.setIsDeleted(1);
            repository.save(integrationConfigStructMapper.toEntity(dto));
            return true;
        }
        return false;
    }
}
