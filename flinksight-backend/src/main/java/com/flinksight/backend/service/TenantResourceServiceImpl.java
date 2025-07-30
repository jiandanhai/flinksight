package com.flinksight.backend.service;

import com.flinksight.backend.domain.TenantResource;
import com.flinksight.backend.mapper.TenantConfigStructMapper;
import com.flinksight.backend.mapper.TenantResourceStructMapper;
import com.flinksight.backend.repository.TenantResourceRepository;
import com.flinksight.common.dto.TenantConfigDTO;
import com.flinksight.common.dto.TenantResourceDTO;
import com.flinksight.common.service.TenantResourceService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional
public class TenantResourceServiceImpl implements TenantResourceService {
    private final TenantResourceRepository repository;
    private final TenantResourceStructMapper mapper;

    @Override
    public TenantResourceDTO assignResourceToTenant(Long tenantId, Long resourceId) {
        TenantResource tr = TenantResource.builder()
            .tenantId(tenantId)
            .resourceId(resourceId)
            .isDeleted(0)
            .build();
        return mapper.toDTO(repository.save(tr));
    }

    @Override
    public boolean removeResourceFromTenant(Long tenantId, Long resourceId) {
        List<TenantResource> list = repository.findByTenantIdAndIsDeleted(tenantId, 0);
        for (TenantResource tr : list) {
            if (tr.getResourceId().equals(resourceId)) {
                tr.setIsDeleted(1);
                repository.save(tr);
                return true;
            }
        }
        return false;
    }

    @Override
    public List<TenantResourceDTO> findByTenantId(Long tenantId) {
        return mapper.toDTOList(repository.findByTenantIdAndIsDeleted(tenantId, 0));

    }

    @Override
    public List<TenantResourceDTO> findByResourceId(Long resourceId) {
        return mapper.toDTOList(repository.findByResourceIdAndIsDeleted(resourceId, 0));
    }

    @Override
    public Optional<TenantResourceDTO> getById(Long id) {
        return repository.findById(id).map(mapper::toDTO).filter(e -> e.getIsDeleted() == 0);
    }

    @Override
    public boolean softDelete(Long id) {
        Optional<TenantResourceDTO> opt = repository.findById(id).map(mapper::toDTO).filter(e -> e.getIsDeleted() == 0);
        if (opt.isPresent()) {
            TenantResourceDTO dto = opt.get();
            dto.setIsDeleted(1);
            repository.save(mapper.toEntity(dto));
            return true;
        }
        return false;
    }
}
