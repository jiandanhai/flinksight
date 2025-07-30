package com.flinksight.backend.service;

import com.flinksight.backend.domain.Permission;
import com.flinksight.backend.domain.Role;
import com.flinksight.backend.mapper.RolePermissionStructMapper;
import com.flinksight.backend.mapper.RoleStructMapper;
import com.flinksight.backend.repository.RoleRepository;
import com.flinksight.backend.security.tenant.TenantRequired;
import com.flinksight.common.dto.RoleDTO;
import com.flinksight.common.dto.RolePermissionDTO;
import com.flinksight.common.service.RoleService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

/**
 * 角色业务实现
 * Role Service Impl
 */
@Service
@RequiredArgsConstructor
@Transactional
@TenantRequired
public class RoleServiceImpl implements RoleService  {

    private final RoleRepository repository;
    private final RoleStructMapper mapper;

    @Override
    public RoleDTO createRole(RoleDTO roleDTO) {
        Role entity = mapper.toEntity(roleDTO);
        entity.setIsDeleted(0);
        Role saved = repository.save(entity);
        return mapper.toDTO(saved);
    }

    @Override
    public Optional<RoleDTO> getRoleById(Long roleId) {
        return repository.findById(roleId).map(mapper::toDTO).filter(e -> e.getIsDeleted() == 0);
    }

    @Override
    public RoleDTO getRoleByCode(String code) {
        return mapper.toDTO(repository.findByCode(code));
    }

    @Override
    public List<RoleDTO> getAllRoles() {
        return mapper.toDTOList(repository.findAll().stream().filter(e -> e.getIsDeleted() == 0).toList());
    }

    @Override
    public boolean softDelete(Long id) {
        Optional<RoleDTO> opt = repository.findById(id).map(mapper::toDTO).filter(e -> e.getIsDeleted() == 0);
        if (opt.isPresent()) {
            RoleDTO dto = opt.get();
            dto.setIsDeleted(1);
            repository.save(mapper.toEntity(dto));
            return true;
        }
        return false;
    }
}
