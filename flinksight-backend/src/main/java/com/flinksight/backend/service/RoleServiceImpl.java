package com.flinksight.backend.service;

import com.flinksight.backend.domain.Role;
import com.flinksight.backend.mapper.RoleStructMapper;
import com.flinksight.backend.repository.RoleRepository;
import com.flinksight.backend.security.tenant.TenantRequired;
import com.flinksight.common.dto.RoleDTO;
import com.flinksight.common.model.PageResult;
import com.flinksight.common.service.RoleService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.stream.Collectors;

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
    private final RoleStructMapper roleStructMapper;

    @Override
    public RoleDTO createRole(RoleDTO roleDTO) {
        Role entity = roleStructMapper.toEntity(roleDTO);
        entity.setIsDeleted(0);
        Role saved = repository.save(entity);
        return roleStructMapper.toDTO(saved);
    }

    @Override
    public Optional<RoleDTO> getRoleById(Long roleId) {
        return repository.findById(roleId).map(roleStructMapper::toDTO).filter(e -> e.getIsDeleted() == 0);
    }

    @Override
    public Optional<RoleDTO> getRoleByCode(String code) {
        return repository.findByCode(code).map(roleStructMapper::toDTO);
    }

    @Override
    public PageResult<RoleDTO> getAllRoles(int page, int size) {
        Page<Role> result = repository.findByIsDeleted(0, PageRequest.of(page, size, Sort.by("id").descending()));
        Page<RoleDTO> dtoPage = result.map(roleStructMapper::toDTO);
        return new PageResult<>(dtoPage);
    }

    @Override
    public RoleDTO update(RoleDTO dto) {
        Role role = repository.findById(dto.getId())
                .orElseThrow(() -> new IllegalArgumentException("角色不存在"));
        role.setName(dto.getName());
        role.setRemark(dto.getRemark());
        role.setUpdatedAt(LocalDateTime.now());
        return roleStructMapper.toDTO(repository.save(role));
    }

    @Override
    public PageResult<RoleDTO> pageList(String name, int page, int size) {
        Page<Role> pg = repository.findByNameAndIsDeleted(
                name == null ? "" : name, 0, PageRequest.of(page, size)
        );
        return new PageResult<>(
                pg.getContent().stream().map(roleStructMapper::toDTO).collect(Collectors.toList()),
                pg.getTotalElements(), page, size
        );
    }

    @Override
    public boolean softDelete(Long id) {
        Optional<RoleDTO> opt = repository.findById(id).map(roleStructMapper::toDTO).filter(e -> e.getIsDeleted() == 0);
        if (opt.isPresent()) {
            RoleDTO dto = opt.get();
            dto.setIsDeleted(1);
            repository.save(roleStructMapper.toEntity(dto));
            return true;
        }
        return false;
    }
}
