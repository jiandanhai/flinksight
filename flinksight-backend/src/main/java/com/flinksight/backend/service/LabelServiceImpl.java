package com.flinksight.backend.service;

import com.flinksight.backend.domain.Label;
import com.flinksight.backend.mapper.LabelStructMapper;
import com.flinksight.backend.repository.LabelRepository;
import com.flinksight.common.dto.LabelDTO;
import com.flinksight.common.model.PageResult;
import com.flinksight.common.service.LabelService;
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
public class LabelServiceImpl implements LabelService {
    private final LabelRepository repository;
    private final LabelStructMapper labelStructMapper;

    @Override
    public LabelDTO createOrUpdate(LabelDTO labelDTO) {
        Label entity = labelStructMapper.toEntity(labelDTO);
        entity.setIsDeleted(0);
        Label saved = repository.save(entity);
        return labelStructMapper.toDTO(saved);
    }

    @Override
    public Optional<LabelDTO> getById(Long id) {
        return repository.findById(id).map(labelStructMapper::toDTO).filter(e -> e.getIsDeleted() == 0);
    }

    @Override
    public PageResult<LabelDTO> findByTenantId(Long tenantId,int page, int size) {
        Page<Label> result = repository.findByTenantIdAndIsDeleted(tenantId,0, PageRequest.of(page, size, Sort.by("id").descending()));
        Page<LabelDTO> dtoPage = result.map(labelStructMapper::toDTO);
        return new PageResult<>(dtoPage);
    }

    @Override
    public boolean softDelete(Long id) {
        Optional<LabelDTO> opt = repository.findById(id).map(labelStructMapper::toDTO).filter(e -> e.getIsDeleted() == 0);
        if (opt.isPresent()) {
            LabelDTO dto = opt.get();
            dto.setIsDeleted(1);
            repository.save(labelStructMapper.toEntity(dto));
            return true;
        }
        return false;
    }
}
