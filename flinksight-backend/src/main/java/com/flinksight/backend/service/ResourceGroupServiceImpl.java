package com.flinksight.backend.service;

import com.flinksight.backend.domain.ResourceGroup;
import com.flinksight.backend.mapper.ResourceGroupStructMapper;
import com.flinksight.backend.repository.ResourceGroupRepository;
import com.flinksight.common.dto.ResourceGroupDTO;
import com.flinksight.common.model.PageResult;
import com.flinksight.common.service.ResourceGroupService;
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
public class ResourceGroupServiceImpl implements ResourceGroupService {
    private final ResourceGroupRepository repository;
    private final ResourceGroupStructMapper resourceGroupStructMapper;

    @Override
    public ResourceGroupDTO createOrUpdate(ResourceGroupDTO resourceGroupDTO) {
        ResourceGroup entity = resourceGroupStructMapper.toEntity(resourceGroupDTO);
        entity.setIsDeleted(0);
        ResourceGroup saved = repository.save(entity);
        return resourceGroupStructMapper.toDTO(saved);
    }

    @Override
    public Optional<ResourceGroupDTO> getById(Long id) {
        return repository.findById(id).map(resourceGroupStructMapper::toDTO).filter(e -> e.getIsDeleted() == 0);
    }

    @Override
    public PageResult<ResourceGroupDTO> getAll(int page, int size) {
        Page<ResourceGroup> result = repository.findByIsDeleted(0, PageRequest.of(page, size, Sort.by("id").descending()));
        Page<ResourceGroupDTO> dtoPage = result.map(resourceGroupStructMapper::toDTO);
        return new PageResult<>(dtoPage);
    }

    @Override
    public PageResult<ResourceGroupDTO> findByTenantId(Long tenantId,int page, int size) {
        Page<ResourceGroup> result = repository.findByTenantIdAndIsDeleted(tenantId,0, PageRequest.of(page, size, Sort.by("id").descending()));
        Page<ResourceGroupDTO> dtoPage = result.map(resourceGroupStructMapper::toDTO);
        return new PageResult<>(dtoPage);
    }

    @Override
    public boolean softDelete(Long id) {
        Optional<ResourceGroupDTO> opt = repository.findById(id).map(resourceGroupStructMapper::toDTO).filter(e -> e.getIsDeleted() == 0);
        if (opt.isPresent()) {
            ResourceGroupDTO dto = opt.get();
            dto.setIsDeleted(1);
            repository.save(resourceGroupStructMapper.toEntity(dto));
            return true;
        }
        return false;
    }
}
