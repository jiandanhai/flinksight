package com.flinksight.backend.service;

import com.flinksight.backend.domain.AuditLog;
import com.flinksight.backend.domain.Dict;
import com.flinksight.backend.mapper.DeptRoleStructMapper;
import com.flinksight.backend.mapper.DictStructMapper;
import com.flinksight.backend.repository.DictRepository;
import com.flinksight.common.dto.DeptRoleDTO;
import com.flinksight.common.dto.DictDTO;
import com.flinksight.common.service.DictService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional
public class DictServiceImpl implements DictService {
    private final DictRepository repository;
    private final DictStructMapper mapper;

    @Override
    public DictDTO createOrUpdate(DictDTO dictDTO) {
        Dict entity = mapper.toEntity(dictDTO);
        entity.setIsDeleted(0);
        Dict saved = repository.save(entity);
        return mapper.toDTO(saved);
    }

    @Override
    public Optional<DictDTO> getById(Long id) {
        return repository.findById(id).map(mapper::toDTO).filter(e -> e.getIsDeleted() == 0);
    }

    @Override
    public List<DictDTO> findByDictType(String dictType) {
        return mapper.toDTOList(repository.findByDictTypeAndIsDeleted(dictType,0));
    }


    @Override
    public boolean softDelete(Long id) {
        Optional<DictDTO> opt = repository.findById(id).map(mapper::toDTO).filter(e -> e.getIsDeleted() == 0);
        if (opt.isPresent()) {
            DictDTO dto = opt.get();
            dto.setIsDeleted(1);
            repository.save(mapper.toEntity(dto));
            return true;
        }
        return false;
    }
}
