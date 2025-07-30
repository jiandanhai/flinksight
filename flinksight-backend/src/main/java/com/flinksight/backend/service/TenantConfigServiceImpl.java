package com.flinksight.backend.service;

import com.flinksight.backend.domain.Tag;
import com.flinksight.backend.domain.TenantConfig;
import com.flinksight.backend.mapper.TagStructMapper;
import com.flinksight.backend.mapper.TenantConfigStructMapper;
import com.flinksight.backend.repository.TenantConfigRepository;
import com.flinksight.common.dto.TagDTO;
import com.flinksight.common.dto.TenantConfigDTO;
import com.flinksight.common.service.TenantConfigService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional
public class TenantConfigServiceImpl implements TenantConfigService {

    private final TenantConfigRepository repository;
    private final TenantConfigStructMapper mapper;

    @Override
    public TenantConfigDTO createOrUpdate(TenantConfigDTO tenantConfigDTO) {
        TenantConfig entity = mapper.toEntity(tenantConfigDTO);
        if (tenantConfigDTO.getId() == null) {
            entity.setCreateTime(LocalDateTime.now());
        }
        entity.setUpdateTime(LocalDateTime.now());
        entity.setIsDeleted(0);
        return mapper.toDTO(repository.save(entity));
    }

    @Override
    public Optional<TenantConfigDTO> getById(Long id) {
        return repository.findById(id).map(mapper::toDTO).filter(e -> e.getIsDeleted() == 0);
    }

    @Override
    public Optional<TenantConfigDTO> getByTenantIdAndConfigKey(Long tenantId, String configKey) {
        return repository.findByTenantIdAndConfigKeyAndIsDeleted(tenantId, configKey, 0).map(mapper::toDTO).filter(e -> e.getIsDeleted() == 0);
    }

    @Override
    public List<TenantConfigDTO> listByTenantId(Long tenantId) {
        return mapper.toDTOList(repository.findByTenantIdAndIsDeleted(tenantId, 0));
    }

    @Override
    public boolean softDelete(Long id) {
        Optional<TenantConfigDTO> opt = repository.findById(id).map(mapper::toDTO).filter(e -> e.getIsDeleted() == 0);
        if (opt.isPresent()) {
            TenantConfigDTO dto = opt.get();
            dto.setIsDeleted(1);
            repository.save(mapper.toEntity(dto));
            return true;
        }
        return false;
    }

    @Override
    public boolean batchSoftDelete(List<Long> ids) {
        List<TenantConfig> configs = repository.findByIdInAndIsDeleted(ids, 0);
        for (TenantConfig config : configs) {
            config.setIsDeleted(1);
            config.setUpdateTime(LocalDateTime.now());
        }
        repository.saveAll(configs);
        return true;
    }
}
