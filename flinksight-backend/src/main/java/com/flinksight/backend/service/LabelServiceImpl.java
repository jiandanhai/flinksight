package com.flinksight.backend.service;

import com.flinksight.backend.common.PageHelpers;
import com.flinksight.backend.domain.Label;
import com.flinksight.backend.mapper.LabelStructMapper;
import com.flinksight.backend.repository.LabelRepository;
import com.flinksight.backend.security.SecurityUtil;
import com.flinksight.common.dto.LabelDTO;
import com.flinksight.common.model.PageResult;
import com.flinksight.common.service.LabelService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
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
    public PageResult<LabelDTO> list(int page, int size) {
        PageRequest pr = PageHelpers.pageRequest(page, size, null, Label.class); // 统一 1→0
        Page<Label> result = repository.findByTenantIdAndIsDeleted(SecurityUtil.getCurrentTenantId(),0, pr);
        return PageHelpers.toPageResult(result, labelStructMapper::toDTO, true); // 返回
    }

    @Override
    public boolean sDelete(Long id) {
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
