package com.flinksight.backend.service;

import com.flinksight.backend.domain.File;
import com.flinksight.backend.domain.IntegrationConfig;
import com.flinksight.backend.mapper.FileStructMapper;
import com.flinksight.backend.mapper.IntegrationConfigStructMapper;
import com.flinksight.backend.repository.IntegrationConfigRepository;
import com.flinksight.common.dto.DataSourceDTO;
import com.flinksight.common.dto.FileDTO;
import com.flinksight.common.dto.IntegrationConfigDTO;
import com.flinksight.common.service.IntegrationConfigService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional
public class IntegrationConfigServiceImpl implements IntegrationConfigService {
    private final IntegrationConfigRepository repository;
    private final IntegrationConfigStructMapper mapper;

    @Override
    public IntegrationConfigDTO createOrUpdate(IntegrationConfigDTO integrationConfigDTO) {
        IntegrationConfig entity = mapper.toEntity(integrationConfigDTO);
        entity.setIsDeleted(0);
        return mapper.toDTO(repository.save(entity));
    }

    @Override
    public Optional<IntegrationConfigDTO> getById(Long id) {
        return repository.findById(id).map(mapper::toDTO).filter(e -> e.getIsDeleted() == 0);
    }

    @Override
    public List<IntegrationConfigDTO> findByTenantId(Long tenantId) {
        return mapper.toDTOList(repository.findByTenantIdAndIsDeleted(tenantId,0));
    }

    @Override
    public List<IntegrationConfigDTO> getAll() {
        return mapper.toDTOList(repository.findAll().stream().filter(e -> e.getIsDeleted() == 0).toList());
    }

    @Override
    public boolean softDelete(Long id) {
        Optional<IntegrationConfigDTO> opt = repository.findById(id).map(mapper::toDTO).filter(e -> e.getIsDeleted() == 0);
        if (opt.isPresent()) {
            IntegrationConfigDTO dto = opt.get();
            dto.setIsDeleted(1);
            repository.save(mapper.toEntity(dto));
            return true;
        }
        return false;
    }
}
