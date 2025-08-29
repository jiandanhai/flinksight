package com.flinksight.backend.service;

import com.flinksight.backend.common.PageHelpers;
import com.flinksight.backend.domain.Role;
import com.flinksight.backend.mapper.RoleStructMapper;
import com.flinksight.backend.repository.RoleRepository;
import com.flinksight.backend.security.SecurityUtil;
import com.flinksight.backend.security.tenant.TenantRequired;
import com.flinksight.common.dto.RoleDTO;
import com.flinksight.common.model.PageResult;
import com.flinksight.common.service.RoleService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
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
        return repository.findByTenantIdAndCode(SecurityUtil.getCurrentTenantId(),code).map(roleStructMapper::toDTO);
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
    public PageResult<RoleDTO> list(String keyword, String name, String code,
                                    Integer page, Integer size) {
        PageRequest pr = PageHelpers.pageRequest(page, size, null, Role.class); // 统一分页+排序

        Page<Role> result = repository.searchByTenantAndFilters(
                SecurityUtil.getCurrentTenantId(),
                (keyword == null || keyword.isBlank()) ? null : keyword.trim(),
                (name == null || name.isBlank()) ? null : name.trim(),
                (code == null || code.isBlank()) ? null : code.trim(),
                pr
        );

        return PageHelpers.toPageResult(result, roleStructMapper::toDTO, true);
    }


    @Override
    public boolean sDelete(Long id) {
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
