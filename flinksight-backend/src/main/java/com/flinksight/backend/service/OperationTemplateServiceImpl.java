package com.flinksight.backend.service;

import com.flinksight.backend.domain.Notification;
import com.flinksight.backend.domain.OperationTemplate;
import com.flinksight.backend.mapper.NotificationStructMapper;
import com.flinksight.backend.mapper.OperationTemplateStructMapper;
import com.flinksight.backend.repository.OperationTemplateRepository;
import com.flinksight.common.dto.NotificationDTO;
import com.flinksight.common.dto.OperationTemplateDTO;
import com.flinksight.common.service.OperationTemplateService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional
public class OperationTemplateServiceImpl implements OperationTemplateService {
    private final OperationTemplateRepository repository;
    private final OperationTemplateStructMapper mapper;

    @Override
    public OperationTemplateDTO createOrUpdate(OperationTemplateDTO operationTemplateDTO) {
        OperationTemplate entity = mapper.toEntity(operationTemplateDTO);
        entity.setIsDeleted(0);
        OperationTemplate saved = repository.save(entity);
        return mapper.toDTO(saved);
    }

    @Override
    public Optional<OperationTemplateDTO> getById(Long id) {
        return repository.findById(id).map(mapper::toDTO).filter(e -> e.getIsDeleted() == 0);
    }

    @Override
    public List<OperationTemplateDTO> getAll() {
        return mapper.toDTOList(repository.findByTenantIdAndIsDeleted(null, 0));
    }

    @Override
    public List<OperationTemplateDTO> findByTenantId(Long tenantId) {
        return mapper.toDTOList(repository.findByTenantIdAndIsDeleted(tenantId, 0));
    }

    @Override
    public boolean softDelete(Long id) {
        Optional<OperationTemplateDTO> opt = repository.findById(id).map(mapper::toDTO).filter(e -> e.getIsDeleted() == 0);
        if (opt.isPresent()) {
            OperationTemplateDTO dto = opt.get();
            dto.setIsDeleted(1);
            repository.save(mapper.toEntity(dto));
            return true;
        }
        return false;
    }
}
