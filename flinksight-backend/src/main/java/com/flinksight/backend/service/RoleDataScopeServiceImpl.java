package com.flinksight.backend.service;

import com.flinksight.backend.domain.RoleDataScope;
import com.flinksight.backend.mapper.RoleDataScopeStructMapper;
import com.flinksight.backend.repository.RoleDataScopeRepository;
import com.flinksight.common.dto.RoleDataScopeDTO;
import com.flinksight.common.model.PageResult;
import com.flinksight.common.service.RoleDataScopeService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional
public class RoleDataScopeServiceImpl implements RoleDataScopeService {
    private final RoleDataScopeRepository repository;
    private final RoleDataScopeStructMapper roleDataScopeStructMapper;

    @Override
    public RoleDataScopeDTO assignDataScopeToRole(Long roleId, Long dataScopeId) {
        RoleDataScope rds = RoleDataScope.builder()
            .roleId(roleId)
            .dataScopeId(dataScopeId)
            .isDeleted(0)
            .build();
        return roleDataScopeStructMapper.toDTO(repository.save(rds));
    }

    @Override
    public boolean removeDataScopeFromRole(Long roleId, Long dataScopeId) {
        List<RoleDataScope> list = repository.findByRoleIdAndDataScopeIdAndIsDeleted(roleId, dataScopeId,0);
        for (RoleDataScope rds : list) {
            if (rds.getDataScopeId().equals(dataScopeId)) {
                rds.setIsDeleted(1);
                repository.save(rds);
                return true;
            }
        }
        return false;
    }

    @Override
    public PageResult<RoleDataScopeDTO> findByRoleId(Long roleId,int page, int size) {
        Page<RoleDataScope> result = repository.findByRoleIdAndIsDeleted(roleId,0, PageRequest.of(page, size, Sort.by("id").descending()));
        Page<RoleDataScopeDTO> dtoPage = result.map(roleDataScopeStructMapper::toDTO);
        return new PageResult<>(dtoPage);
    }

    @Override
    public PageResult<RoleDataScopeDTO> findByDataScopeId(Long dataScopeId,int page, int size) {
        Page<RoleDataScope> result = repository.findByDataScopeIdAndIsDeleted(dataScopeId,0, PageRequest.of(page, size, Sort.by("id").descending()));
        Page<RoleDataScopeDTO> dtoPage = result.map(roleDataScopeStructMapper::toDTO);
        return new PageResult<>(dtoPage);
    }

    @Override
    public Optional<RoleDataScopeDTO> getById(Long id) {
        return repository.findById(id).map(roleDataScopeStructMapper::toDTO).filter(e -> e.getIsDeleted() == 0);
    }

    @Override
    public boolean softDelete(Long id) {
        Optional<RoleDataScopeDTO> opt = repository.findById(id).map(roleDataScopeStructMapper::toDTO).filter(e -> e.getIsDeleted() == 0);
        if (opt.isPresent()) {
            RoleDataScopeDTO dto = opt.get();
            dto.setIsDeleted(1);
            repository.save(roleDataScopeStructMapper.toEntity(dto));
            return true;
        }
        return false;
    }
}
