package com.flinksight.backend.service;

import com.flinksight.backend.domain.Role;
import com.flinksight.backend.domain.SysParam;
import com.flinksight.backend.mapper.RoleStructMapper;
import com.flinksight.backend.mapper.SysParamStructMapper;
import com.flinksight.backend.repository.SysParamRepository;
import com.flinksight.common.dto.RoleDTO;
import com.flinksight.common.dto.SysParamDTO;
import com.flinksight.common.service.SysParamService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional
public class SysParamServiceImpl implements SysParamService {
    private final SysParamRepository repository;
    private final SysParamStructMapper mapper;
    @Override
    public SysParamDTO createOrUpdate(SysParamDTO sysParamDTO) {
        SysParam entity = mapper.toEntity(sysParamDTO);
        entity.setIsDeleted(0);
        SysParam saved = repository.save(entity);
        return mapper.toDTO(saved);
    }

    @Override
    public Optional<SysParamDTO> getById(Long id) {
        return repository.findById(id).map(mapper::toDTO).filter(e -> e.getIsDeleted() == 0);
    }

    @Override
    public List<SysParamDTO> getAll() {
        return mapper.toDTOList(repository.findAll().stream().filter(e -> e.getIsDeleted() == 0).toList());
    }

    @Override
    public boolean softDelete(Long id) {
        Optional<SysParamDTO> opt = repository.findById(id).map(mapper::toDTO).filter(e -> e.getIsDeleted() == 0);
        if (opt.isPresent()) {
            SysParamDTO dto = opt.get();
            dto.setIsDeleted(1);
            repository.save(mapper.toEntity(dto));
            return true;
        }
        return false;
    }
}
