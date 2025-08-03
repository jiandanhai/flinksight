package com.flinksight.backend.service;

import com.flinksight.backend.domain.OperationTemplate;
import com.flinksight.backend.mapper.OperationTemplateStructMapper;
import com.flinksight.backend.repository.OperationTemplateRepository;
import com.flinksight.common.dto.OperationTemplateDTO;
import com.flinksight.common.model.PageResult;
import com.flinksight.common.service.OperationTemplateService;
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
public class OperationTemplateServiceImpl implements OperationTemplateService {
    private final OperationTemplateRepository repository;
    private final OperationTemplateStructMapper operationTemplateStructMapper;

    @Override
    public OperationTemplateDTO createOrUpdate(OperationTemplateDTO operationTemplateDTO) {
        OperationTemplate entity = operationTemplateStructMapper.toEntity(operationTemplateDTO);
        entity.setIsDeleted(0);
        OperationTemplate saved = repository.save(entity);
        return operationTemplateStructMapper.toDTO(saved);
    }

    @Override
    public Optional<OperationTemplateDTO> getById(Long id) {
        return repository.findById(id).map(operationTemplateStructMapper::toDTO).filter(e -> e.getIsDeleted() == 0);
    }

    @Override
    public PageResult<OperationTemplateDTO> getAll(int page, int size) {
        Page<OperationTemplate> result = repository.findByIsDeleted(0, PageRequest.of(page, size, Sort.by("id").descending()));
        Page<OperationTemplateDTO> dtoPage = result.map(operationTemplateStructMapper::toDTO);
        return new PageResult<>(dtoPage);
    }

    @Override
    public PageResult<OperationTemplateDTO> findByTenantId(Long tenantId,int page, int size) {
        Page<OperationTemplate> result = repository.findByTenantIdAndIsDeleted(tenantId,0, PageRequest.of(page, size, Sort.by("id").descending()));
        Page<OperationTemplateDTO> dtoPage = result.map(operationTemplateStructMapper::toDTO);
        return new PageResult<>(dtoPage);
    }

    @Override
    public boolean softDelete(Long id) {
        Optional<OperationTemplateDTO> opt = repository.findById(id).map(operationTemplateStructMapper::toDTO).filter(e -> e.getIsDeleted() == 0);
        if (opt.isPresent()) {
            OperationTemplateDTO dto = opt.get();
            dto.setIsDeleted(1);
            repository.save(operationTemplateStructMapper.toEntity(dto));
            return true;
        }
        return false;
    }
}
