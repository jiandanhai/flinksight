package com.flinksight.backend.service;

import com.flinksight.backend.domain.Resource;
import com.flinksight.backend.mapper.ResourceStructMapper;
import com.flinksight.backend.repository.ResourceRepository;
import com.flinksight.common.dto.ResourceDTO;
import com.flinksight.common.model.PageResult;
import com.flinksight.common.service.ResourceService;
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
public class ResourceServiceImpl implements ResourceService {
    private final ResourceRepository repository;
    private final ResourceStructMapper resourceStructMapper;

    @Override
    public ResourceDTO createOrUpdate(ResourceDTO resourceDTO) {
        Resource entity = resourceStructMapper.toEntity(resourceDTO);
        entity.setIsDeleted(0);
        Resource saved = repository.save(entity);
        return resourceStructMapper.toDTO(saved);
    }

    @Override
    public Optional<ResourceDTO> getById(Long id) {
        return repository.findById(id).map(resourceStructMapper::toDTO).filter(e -> e.getIsDeleted() == 0);
    }

    @Override
    public PageResult<ResourceDTO> getAll(int page, int size) {
        Page<Resource> result = repository.findByIsDeleted(0, PageRequest.of(page, size, Sort.by("id").descending()));
        Page<ResourceDTO> dtoPage = result.map(resourceStructMapper::toDTO);
        return new PageResult<>(dtoPage);
    }

    @Override
    public PageResult<ResourceDTO> findByTenantId(Long tenantId,int page, int size) {
        Page<Resource> result = repository.findByTenantIdAndIsDeleted(tenantId,0, PageRequest.of(page, size, Sort.by("id").descending()));
        Page<ResourceDTO> dtoPage = result.map(resourceStructMapper::toDTO);
        return new PageResult<>(dtoPage);
    }

    @Override
    public boolean softDelete(Long id) {
        Optional<ResourceDTO> opt = repository.findById(id).map(resourceStructMapper::toDTO).filter(e -> e.getIsDeleted() == 0);
        if (opt.isPresent()) {
            ResourceDTO dto = opt.get();
            dto.setIsDeleted(1);
            repository.save(resourceStructMapper.toEntity(dto));
            return true;
        }
        return false;
    }
}
