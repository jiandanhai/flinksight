package com.flinksight.backend.service;

import com.flinksight.backend.domain.Resource;
import com.flinksight.backend.domain.ResourceGroup;
import com.flinksight.backend.mapper.ResourceLabelStructMapper;
import com.flinksight.backend.mapper.ResourceStructMapper;
import com.flinksight.backend.repository.ResourceRepository;
import com.flinksight.common.dto.ResourceDTO;
import com.flinksight.common.dto.ResourceGroupDTO;
import com.flinksight.common.dto.ResourceLabelDTO;
import com.flinksight.common.service.ResourceService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional
public class ResourceServiceImpl implements ResourceService {
    private final ResourceRepository repository;
    private final ResourceStructMapper mapper;

    @Override
    public ResourceDTO createOrUpdate(ResourceDTO resourceDTO) {
        Resource entity = mapper.toEntity(resourceDTO);
        entity.setIsDeleted(0);
        Resource saved = repository.save(entity);
        return mapper.toDTO(saved);
    }

    @Override
    public Optional<ResourceDTO> getById(Long id) {
        return repository.findById(id).map(mapper::toDTO).filter(e -> e.getIsDeleted() == 0);
    }

    @Override
    public List<ResourceDTO> getAll() {
        return mapper.toDTOList(repository.findByTenantIdAndIsDeleted(null, 0));
    }

    @Override
    public List<ResourceDTO> findByTenantId(Long tenantId) {
        return mapper.toDTOList(repository.findByTenantIdAndIsDeleted(tenantId, 0));
    }

    @Override
    public boolean softDelete(Long id) {
        Optional<ResourceDTO> opt = repository.findById(id).map(mapper::toDTO).filter(e -> e.getIsDeleted() == 0);
        if (opt.isPresent()) {
            ResourceDTO dto = opt.get();
            dto.setIsDeleted(1);
            repository.save(mapper.toEntity(dto));
            return true;
        }
        return false;
    }
}
