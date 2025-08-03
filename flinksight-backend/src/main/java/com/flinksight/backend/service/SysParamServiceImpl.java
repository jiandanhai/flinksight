package com.flinksight.backend.service;

import com.flinksight.backend.domain.SysParam;
import com.flinksight.backend.mapper.SysParamStructMapper;
import com.flinksight.backend.repository.SysParamRepository;
import com.flinksight.common.dto.SysParamDTO;
import com.flinksight.common.model.PageResult;
import com.flinksight.common.service.SysParamService;
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
public class SysParamServiceImpl implements SysParamService {
    private final SysParamRepository repository;
    private final SysParamStructMapper sysParamStructMapper;
    @Override
    public SysParamDTO createOrUpdate(SysParamDTO sysParamDTO) {
        SysParam entity = sysParamStructMapper.toEntity(sysParamDTO);
        entity.setIsDeleted(0);
        SysParam saved = repository.save(entity);
        return sysParamStructMapper.toDTO(saved);
    }

    @Override
    public Optional<SysParamDTO> getById(Long id) {
        return repository.findById(id).map(sysParamStructMapper::toDTO).filter(e -> e.getIsDeleted() == 0);
    }

    @Override
    public PageResult<SysParamDTO> getAll(int page, int size) {
        Page<SysParam> result = repository.findByIsDeleted(0, PageRequest.of(page, size, Sort.by("id").descending()));
        Page<SysParamDTO> dtoPage = result.map(sysParamStructMapper::toDTO);
        return new PageResult<>(dtoPage);
    }

    @Override
    public boolean softDelete(Long id) {
        Optional<SysParamDTO> opt = repository.findById(id).map(sysParamStructMapper::toDTO).filter(e -> e.getIsDeleted() == 0);
        if (opt.isPresent()) {
            SysParamDTO dto = opt.get();
            dto.setIsDeleted(1);
            repository.save(sysParamStructMapper.toEntity(dto));
            return true;
        }
        return false;
    }
}
