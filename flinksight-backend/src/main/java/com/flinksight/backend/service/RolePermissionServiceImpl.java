package com.flinksight.backend.service;

import com.flinksight.backend.domain.RolePermission;
import com.flinksight.backend.mapper.RolePermissionStructMapper;
import com.flinksight.backend.repository.RolePermissionRepository;
import com.flinksight.common.dto.RolePermissionDTO;
import com.flinksight.common.model.PageResult;
import com.flinksight.common.service.RolePermissionService;
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
public class RolePermissionServiceImpl implements RolePermissionService {
    private final RolePermissionRepository repository;
    private final RolePermissionStructMapper rolePermissionStructMapper;
    @Override
    public RolePermissionDTO assignPermissionToRole(Long roleId, String permissionCode) {
        RolePermission rp = RolePermission.builder()
            .roleId(roleId)
            .permissionCode(permissionCode)
            .isDeleted(0)
            .build();
        return rolePermissionStructMapper.toDTO(repository.save(rp));
    }

    @Override
    public boolean removePermissionFromRole(Long roleId, String permissionCode) {
        List<RolePermission> list = repository.findByRoleIdAndPermissionCodeAndIsDeleted(roleId,permissionCode, 0);
        for (RolePermission rp : list) {
            if (rp.getPermissionCode().equals(permissionCode)) {
                rp.setIsDeleted(1);
                repository.save(rp);
                return true;
            }
        }
        return false;
    }

    @Override
    public PageResult<RolePermissionDTO> findByRoleId(Long roleId,int page, int size) {
        Page<RolePermission> result = repository.findByRoleIdAndIsDeleted(roleId,0, PageRequest.of(page, size, Sort.by("id").descending()));
        Page<RolePermissionDTO> dtoPage = result.map(rolePermissionStructMapper::toDTO);
        return new PageResult<>(dtoPage);
    }

    @Override
    public PageResult<RolePermissionDTO> findByPermissionCode(String permissionCode,int page, int size) {
        Page<RolePermission> result = repository.findByPermissionCodeAndIsDeleted(permissionCode,0, PageRequest.of(page, size, Sort.by("id").descending()));
        Page<RolePermissionDTO> dtoPage = result.map(rolePermissionStructMapper::toDTO);
        return new PageResult<>(dtoPage);
    }

    @Override
    public Optional<RolePermissionDTO> getById(Long id) {
        return repository.findById(id).map(rolePermissionStructMapper::toDTO).filter(e -> e.getIsDeleted() == 0);
    }

    @Override
    public boolean softDelete(Long id) {
        Optional<RolePermissionDTO> opt = repository.findById(id).map(rolePermissionStructMapper::toDTO).filter(e -> e.getIsDeleted() == 0);
        if (opt.isPresent()) {
            RolePermissionDTO dto = opt.get();
            dto.setIsDeleted(1);
            repository.save(rolePermissionStructMapper.toEntity(dto));
            return true;
        }
        return false;
    }
}
