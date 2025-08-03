package com.flinksight.backend.service;

import com.flinksight.backend.domain.Tag;
import com.flinksight.backend.mapper.TagStructMapper;
import com.flinksight.backend.repository.TagRepository;
import com.flinksight.common.dto.TagDTO;
import com.flinksight.common.model.PageResult;
import com.flinksight.common.service.TagService;
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
public class TagServiceImpl implements TagService {
    private final TagRepository repository;
    private final TagStructMapper tagStructMapper;

    @Override
    public TagDTO createOrUpdate(TagDTO tagDTO) {
        Tag entity = tagStructMapper.toEntity(tagDTO);
        entity.setIsDeleted(0);
        Tag saved = repository.save(entity);
        return tagStructMapper.toDTO(saved);
    }

    @Override
    public Optional<TagDTO> getById(Long id) {
        return repository.findById(id).map(tagStructMapper::toDTO).filter(e -> e.getIsDeleted() == 0);
    }

    @Override
    public PageResult<TagDTO> getAll(int page, int size) {
        Page<Tag> result = repository.findByIsDeleted(0, PageRequest.of(page, size, Sort.by("id").descending()));
        Page<TagDTO> dtoPage = result.map(tagStructMapper::toDTO);
        return new PageResult<>(dtoPage);
    }

    @Override
    public PageResult<TagDTO> findByTenantId(Long tenantId,int page, int size) {
        Page<Tag> result = repository.findByTenantIdAndIsDeleted(tenantId,0, PageRequest.of(page, size, Sort.by("id").descending()));
        Page<TagDTO> dtoPage = result.map(tagStructMapper::toDTO);
        return new PageResult<>(dtoPage);
    }

    @Override
    public boolean softDelete(Long id) {
        Optional<TagDTO> opt = repository.findById(id).map(tagStructMapper::toDTO).filter(e -> e.getIsDeleted() == 0);
        if (opt.isPresent()) {
            TagDTO dto = opt.get();
            dto.setIsDeleted(1);
            repository.save(tagStructMapper.toEntity(dto));
            return true;
        }
        return false;
    }
}
