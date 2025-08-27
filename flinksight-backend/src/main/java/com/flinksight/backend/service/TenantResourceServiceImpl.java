package com.flinksight.backend.service;

import com.flinksight.backend.common.PageHelpers;
import com.flinksight.backend.domain.TenantResource;
import com.flinksight.backend.mapper.TenantResourceStructMapper;
import com.flinksight.backend.repository.TenantResourceRepository;
import com.flinksight.common.dto.TenantResourceDTO;
import com.flinksight.common.model.PageResult;
import com.flinksight.common.service.TenantResourceService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional
public class TenantResourceServiceImpl implements TenantResourceService {
    private final TenantResourceRepository repository;
    private final TenantResourceStructMapper tenantResourceStructMapper;

    @Override
    public TenantResourceDTO assignResourceToTenant(Long tenantId, Long resourceId) {
        TenantResource tr = TenantResource.builder()
            .tenantId(tenantId)
            .resourceId(resourceId)
            .isDeleted(0)
            .build();
        return tenantResourceStructMapper.toDTO(repository.save(tr));
    }

    @Override
    public boolean removeResourceFromTenant(Long tenantId, Long resourceId) {
        List<TenantResource> list = repository.findByTenantIdAndResourceIdAndIsDeleted(tenantId, resourceId,0);
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
    public PageResult<TenantResourceDTO> list(Long tenantId, Long resourceId, int page, int size) {
        PageRequest pr = PageHelpers.pageRequest(page, size, null, TenantResource.class); // 统一 1→0
        Page<TenantResource> result = repository.pageQuery(tenantId, resourceId, pr);
        return PageHelpers.toPageResult(result, tenantResourceStructMapper::toDTO, true); // 返

    }

    @Override
    public Optional<TenantResourceDTO> getById(Long id) {
        return repository.findById(id).map(tenantResourceStructMapper::toDTO).filter(e -> e.getIsDeleted() == 0);
    }

    @Override
    public boolean sDelete(Long id) {
        Optional<TenantResourceDTO> opt = repository.findById(id).map(tenantResourceStructMapper::toDTO).filter(e -> e.getIsDeleted() == 0);
        if (opt.isPresent()) {
            TenantResourceDTO dto = opt.get();
            dto.setIsDeleted(1);
            repository.save(tenantResourceStructMapper.toEntity(dto));
            return true;
        }
        return false;
    }
}
