package com.flinksight.backend.service;

import com.flinksight.backend.domain.TenantConfig;
import com.flinksight.backend.mapper.TenantConfigStructMapper;
import com.flinksight.backend.repository.TenantConfigRepository;
import com.flinksight.common.dto.TenantConfigDTO;
import com.flinksight.common.model.PageResult;
import com.flinksight.common.service.TenantConfigService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional
public class TenantConfigServiceImpl implements TenantConfigService {

    private final TenantConfigRepository repository;
    private final TenantConfigStructMapper tenantConfigStructMapper;

    @Override
    public TenantConfigDTO createOrUpdate(TenantConfigDTO tenantConfigDTO) {
        TenantConfig entity = tenantConfigStructMapper.toEntity(tenantConfigDTO);
        if (tenantConfigDTO.getId() == null) {
            entity.setCreateTime(LocalDateTime.now());
        }
        entity.setUpdateTime(LocalDateTime.now());
        entity.setIsDeleted(0);
        return tenantConfigStructMapper.toDTO(repository.save(entity));
    }

    @Override
    public Optional<TenantConfigDTO> getById(Long id) {
        return repository.findById(id).map(tenantConfigStructMapper::toDTO).filter(e -> e.getIsDeleted() == 0);
    }

    @Override
    public Optional<TenantConfigDTO> getByTenantIdAndConfigKey(Long tenantId, String configKey) {
        return repository.findByTenantIdAndConfigKeyAndIsDeleted(tenantId, configKey, 0).map(tenantConfigStructMapper::toDTO).filter(e -> e.getIsDeleted() == 0);
    }

    @Override
    public PageResult<TenantConfigDTO> listByTenantId(Long tenantId,int page, int size) {
        Page<TenantConfig> result = repository.findByTenantIdAndIsDeleted(tenantId,0, PageRequest.of(page, size, Sort.by("id").descending()));
        Page<TenantConfigDTO> dtoPage = result.map(tenantConfigStructMapper::toDTO);
        return new PageResult<>(dtoPage);
    }

    @Override
    public boolean softDelete(Long id) {
        Optional<TenantConfigDTO> opt = repository.findById(id).map(tenantConfigStructMapper::toDTO).filter(e -> e.getIsDeleted() == 0);
        if (opt.isPresent()) {
            TenantConfigDTO dto = opt.get();
            dto.setIsDeleted(1);
            repository.save(tenantConfigStructMapper.toEntity(dto));
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
