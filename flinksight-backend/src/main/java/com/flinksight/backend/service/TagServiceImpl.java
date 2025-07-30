package com.flinksight.backend.service;

import com.flinksight.backend.domain.SysParam;
import com.flinksight.backend.domain.Tag;
import com.flinksight.backend.mapper.SysParamStructMapper;
import com.flinksight.backend.mapper.TagStructMapper;
import com.flinksight.backend.repository.TagRepository;
import com.flinksight.common.dto.SysParamDTO;
import com.flinksight.common.dto.TagDTO;
import com.flinksight.common.service.TagService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional
public class TagServiceImpl implements TagService {
    private final TagRepository repository;
    private final TagStructMapper mapper;

    @Override
    public TagDTO createOrUpdate(TagDTO tagDTO) {
        Tag entity = mapper.toEntity(tagDTO);
        entity.setIsDeleted(0);
        Tag saved = repository.save(entity);
        return mapper.toDTO(saved);
    }

    @Override
    public Optional<TagDTO> getById(Long id) {
        return repository.findById(id).map(mapper::toDTO).filter(e -> e.getIsDeleted() == 0);
    }

    @Override
    public List<TagDTO> getAll() {
        return mapper.toDTOList(repository.findAll().stream().filter(e -> e.getIsDeleted() == 0).toList());
    }

    @Override
    public List<TagDTO> findByTenantId(Long tenantId) {
        return mapper.toDTOList(repository.findByTenantIdAndIsDeleted(tenantId, 0));
    }

    @Override
    public boolean softDelete(Long id) {
        Optional<TagDTO> opt = repository.findById(id).map(mapper::toDTO).filter(e -> e.getIsDeleted() == 0);
        if (opt.isPresent()) {
            TagDTO dto = opt.get();
            dto.setIsDeleted(1);
            repository.save(mapper.toEntity(dto));
            return true;
        }
        return false;
    }
}
