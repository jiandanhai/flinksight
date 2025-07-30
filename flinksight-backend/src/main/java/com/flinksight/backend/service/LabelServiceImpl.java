package com.flinksight.backend.service;

import com.flinksight.backend.domain.Label;
import com.flinksight.backend.mapper.LabelStructMapper;
import com.flinksight.backend.repository.LabelRepository;
import com.flinksight.common.dto.JobDTO;
import com.flinksight.common.dto.LabelDTO;
import com.flinksight.common.service.LabelService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional
public class LabelServiceImpl implements LabelService {
    private final LabelRepository repository;
    private final LabelStructMapper mapper;

    @Override
    public LabelDTO createOrUpdate(LabelDTO labelDTO) {
        Label entity = mapper.toEntity(labelDTO);
        entity.setIsDeleted(0);
        Label saved = repository.save(entity);
        return mapper.toDTO(saved);
    }

    @Override
    public Optional<LabelDTO> getById(Long id) {
        return repository.findById(id).map(mapper::toDTO).filter(e -> e.getIsDeleted() == 0);
    }

    @Override
    public List<LabelDTO> findByTenantId(Long tenantId) {
        return mapper.toDTOList(repository.findByTenantIdAndIsDeleted(tenantId, 0));
    }

    @Override
    public boolean softDelete(Long id) {
        Optional<LabelDTO> opt = repository.findById(id).map(mapper::toDTO).filter(e -> e.getIsDeleted() == 0);
        if (opt.isPresent()) {
            LabelDTO dto = opt.get();
            dto.setIsDeleted(1);
            repository.save(mapper.toEntity(dto));
            return true;
        }
        return false;
    }
}
