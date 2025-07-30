package com.flinksight.backend.service;

import com.flinksight.backend.domain.RoleDataScope;
import com.flinksight.backend.mapper.ResourceStructMapper;
import com.flinksight.backend.mapper.RoleDataScopeStructMapper;
import com.flinksight.backend.repository.RoleDataScopeRepository;
import com.flinksight.common.dto.ResourceDTO;
import com.flinksight.common.dto.RoleDataScopeDTO;
import com.flinksight.common.service.RoleDataScopeService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional
public class RoleDataScopeServiceImpl implements RoleDataScopeService {
    private final RoleDataScopeRepository repository;
    private final RoleDataScopeStructMapper mapper;

    @Override
    public RoleDataScopeDTO assignDataScopeToRole(Long roleId, Long dataScopeId) {
        RoleDataScope rds = RoleDataScope.builder()
            .roleId(roleId)
            .dataScopeId(dataScopeId)
            .isDeleted(0)
            .build();
        return mapper.toDTO(repository.save(rds));
    }

    @Override
    public boolean removeDataScopeFromRole(Long roleId, Long dataScopeId) {
        List<RoleDataScope> list = repository.findByRoleIdAndIsDeleted(roleId, 0);
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
    public List<RoleDataScopeDTO> findByRoleId(Long roleId) {
        return mapper.toDTOList(repository.findByRoleIdAndIsDeleted(roleId, 0));
    }

    @Override
    public List<RoleDataScopeDTO> findByDataScopeId(Long dataScopeId) {
        return mapper.toDTOList(repository.findByDataScopeIdAndIsDeleted(dataScopeId, 0));
    }

    @Override
    public Optional<RoleDataScopeDTO> getById(Long id) {
        return repository.findById(id).map(mapper::toDTO).filter(e -> e.getIsDeleted() == 0);
    }

    @Override
    public boolean softDelete(Long id) {
        Optional<RoleDataScopeDTO> opt = repository.findById(id).map(mapper::toDTO).filter(e -> e.getIsDeleted() == 0);
        if (opt.isPresent()) {
            RoleDataScopeDTO dto = opt.get();
            dto.setIsDeleted(1);
            repository.save(mapper.toEntity(dto));
            return true;
        }
        return false;
    }
}
