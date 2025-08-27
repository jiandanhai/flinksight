package com.flinksight.backend.service;

import com.flinksight.backend.common.PageHelpers;
import com.flinksight.backend.domain.Dict;
import com.flinksight.backend.mapper.DictStructMapper;
import com.flinksight.backend.repository.DictRepository;
import com.flinksight.common.dto.DictDTO;
import com.flinksight.common.model.PageResult;
import com.flinksight.common.service.DictService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional
public class DictServiceImpl implements DictService {
    private final DictRepository repository;
    private final DictStructMapper dictStructMapper;

    @Override
    public DictDTO createOrUpdate(DictDTO dictDTO) {
        Dict entity = dictStructMapper.toEntity(dictDTO);
        entity.setIsDeleted(0);
        Dict saved = repository.save(entity);
        return dictStructMapper.toDTO(saved);
    }

    @Override
    public Optional<DictDTO> getById(Long id) {
        return repository.findById(id).map(dictStructMapper::toDTO).filter(e -> e.getIsDeleted() == 0);
    }

    @Override
    public PageResult<DictDTO> list(String dictType,int page, int size) {
        PageRequest pr = PageHelpers.pageRequest(page, size, null, Dict.class); // 统一 1→0
        Page<Dict> result = repository.findByDictTypeAndIsDeleted( dictType,0, pr);
        return PageHelpers.toPageResult(result, dictStructMapper::toDTO, true); // 返回 1-ba
    }


    @Override
    public boolean sDelete(Long id) {
        Optional<DictDTO> opt = repository.findById(id).map(dictStructMapper::toDTO).filter(e -> e.getIsDeleted() == 0);
        if (opt.isPresent()) {
            DictDTO dto = opt.get();
            dto.setIsDeleted(1);
            repository.save(dictStructMapper.toEntity(dto));
            return true;
        }
        return false;
    }
}
