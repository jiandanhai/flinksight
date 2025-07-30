package com.flinksight.backend.service;

import com.flinksight.backend.domain.Permission;
import com.flinksight.backend.domain.ResourceGroup;
import com.flinksight.backend.mapper.PermissionStructMapper;
import com.flinksight.backend.mapper.ResourceGroupStructMapper;
import com.flinksight.backend.repository.ResourceGroupRepository;
import com.flinksight.common.dto.OperationTemplateDTO;
import com.flinksight.common.dto.ProfileDTO;
import com.flinksight.common.dto.ResourceGroupDTO;
import com.flinksight.common.service.ResourceGroupService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional
public class ResourceGroupServiceImpl implements ResourceGroupService {
    private final ResourceGroupRepository repository;
    private final ResourceGroupStructMapper mapper;

    @Override
    public ResourceGroupDTO createOrUpdate(ResourceGroupDTO resourceGroupDTO) {
        ResourceGroup entity = mapper.toEntity(resourceGroupDTO);
        entity.setIsDeleted(0);
        ResourceGroup saved = repository.save(entity);
        return mapper.toDTO(saved);
    }

    @Override
    public Optional<ResourceGroupDTO> getById(Long id) {
        return repository.findById(id).map(mapper::toDTO).filter(e -> e.getIsDeleted() == 0);
    }

    @Override
    public List<ResourceGroupDTO> getAll() {
        return mapper.toDTOList(repository.findByTenantIdAndIsDeleted(null, 0));
    }

    @Override
    public List<ResourceGroupDTO> findByTenantId(Long tenantId) {
        return mapper.toDTOList(repository.findByTenantIdAndIsDeleted(tenantId, 0));
    }

    @Override
    public boolean softDelete(Long id) {
        Optional<ResourceGroupDTO> opt = repository.findById(id).map(mapper::toDTO).filter(e -> e.getIsDeleted() == 0);
        if (opt.isPresent()) {
            ResourceGroupDTO dto = opt.get();
            dto.setIsDeleted(1);
            repository.save(mapper.toEntity(dto));
            return true;
        }
        return false;
    }
}
