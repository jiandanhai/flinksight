package com.flinksight.backend.service;

import com.flinksight.backend.common.PageHelpers;
import com.flinksight.backend.domain.ResourceLabel;
import com.flinksight.backend.mapper.ResourceLabelStructMapper;
import com.flinksight.backend.repository.ResourceLabelRepository;
import com.flinksight.common.dto.ResourceLabelDTO;
import com.flinksight.common.model.PageResult;
import com.flinksight.common.service.ResourceLabelService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional
public class ResourceLabelServiceImpl implements ResourceLabelService {
    private final ResourceLabelRepository repository;
    private final ResourceLabelStructMapper resourceLabelStructMapper;

    @Override
    public ResourceLabelDTO assignLabelToResource(Long resourceId, Long labelId) {
        ResourceLabel rl = ResourceLabel.builder()
            .resourceId(resourceId)
            .labelId(labelId)
            .isDeleted(0)
            .build();
        repository.save(rl);
        return resourceLabelStructMapper.toDTO(rl);
    }

    @Override
    public boolean removeLabelFromResource(Long resourceId,Long labelId) {
        List<ResourceLabel> list = repository.findByResourceIdAndLabelIdAndIsDeleted(resourceId,labelId, 0);
        for (ResourceLabel rl : list) {
            if (rl.getLabelId().equals(labelId)) {
                rl.setIsDeleted(1);
                repository.save(rl);
                return true;
            }
        }
        return false;
    }

    @Override
    public PageResult<ResourceLabelDTO> list(Long resourceId, Long labelId, int page, int size) {
        PageRequest pr = PageHelpers.pageRequest(page, size, null, ResourceLabel.class); // 统一 1→0
        Page<ResourceLabel> result = repository.pageQuery(resourceId, labelId, pr);
        return PageHelpers.toPageResult(result, resourceLabelStructMapper::toDTO, true); // 返回
    }

    @Override
    public Optional<ResourceLabelDTO> getById(Long id) {
        return repository.findById(id).map(resourceLabelStructMapper::toDTO).filter(e -> e.getIsDeleted() == 0);
    }

    @Override
    public boolean sDelete(Long id) {
        Optional<ResourceLabelDTO> opt = repository.findById(id).map(resourceLabelStructMapper::toDTO).filter(e -> e.getIsDeleted() == 0);
        if (opt.isPresent()) {
            ResourceLabelDTO dto = opt.get();
            dto.setIsDeleted(1);
            repository.save(resourceLabelStructMapper.toEntity(dto));
            return true;
        }
        return false;
    }
}
