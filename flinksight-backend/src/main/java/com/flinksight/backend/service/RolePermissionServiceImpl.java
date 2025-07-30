package com.flinksight.backend.service;

import com.flinksight.backend.domain.RolePermission;
import com.flinksight.backend.mapper.RoleMenuStructMapper;
import com.flinksight.backend.mapper.RolePermissionStructMapper;
import com.flinksight.backend.repository.RolePermissionRepository;
import com.flinksight.common.dto.RoleMenuDTO;
import com.flinksight.common.dto.RolePermissionDTO;
import com.flinksight.common.service.RolePermissionService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional
public class RolePermissionServiceImpl implements RolePermissionService {
    private final RolePermissionRepository repository;
    private final RolePermissionStructMapper mapper;
    @Override
    public RolePermissionDTO assignPermissionToRole(Long roleId, Long permissionId) {
        RolePermission rp = RolePermission.builder()
            .roleId(roleId)
            .permissionId(permissionId)
            .isDeleted(0)
            .build();
        return mapper.toDTO(repository.save(rp));
    }

    @Override
    public boolean removePermissionFromRole(Long roleId, Long permissionId) {
        List<RolePermission> list = repository.findByRoleIdAndIsDeleted(roleId, 0);
        for (RolePermission rp : list) {
            if (rp.getPermissionId().equals(permissionId)) {
                rp.setIsDeleted(1);
                repository.save(rp);
                return true;
            }
        }
        return false;
    }

    @Override
    public List<RolePermissionDTO> findByRoleId(Long roleId) {
        return mapper.toDTOList(repository.findByRoleIdAndIsDeleted(roleId, 0));
    }

    @Override
    public List<RolePermissionDTO> findByPermissionId(Long permissionId) {
        return mapper.toDTOList(repository.findByPermissionIdAndIsDeleted(permissionId, 0));
    }

    @Override
    public Optional<RolePermissionDTO> getById(Long id) {
        return repository.findById(id).map(mapper::toDTO).filter(e -> e.getIsDeleted() == 0);
    }

    @Override
    public boolean softDelete(Long id) {
        Optional<RolePermissionDTO> opt = repository.findById(id).map(mapper::toDTO).filter(e -> e.getIsDeleted() == 0);
        if (opt.isPresent()) {
            RolePermissionDTO dto = opt.get();
            dto.setIsDeleted(1);
            repository.save(mapper.toEntity(dto));
            return true;
        }
        return false;
    }
}
