package com.flinksight.backend.service;

import com.flinksight.backend.domain.OperationTemplate;
import com.flinksight.backend.domain.Permission;
import com.flinksight.backend.mapper.OperationTemplateStructMapper;
import com.flinksight.backend.mapper.PermissionStructMapper;
import com.flinksight.backend.repository.PermissionRepository;
import com.flinksight.common.dto.OperationTemplateDTO;
import com.flinksight.common.dto.PermissionDTO;
import com.flinksight.common.service.PermissionService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

/**
 * 权限点管理服务实现
 * 只负责权限点元数据相关逻辑
 */
@Service
@RequiredArgsConstructor
public class PermissionServiceImpl implements PermissionService {

    private final PermissionRepository repository;
    private final PermissionStructMapper mapper;

    @Override
    public PermissionDTO createPermission(PermissionDTO permissionDTO) {
        Permission entity = mapper.toEntity(permissionDTO);
        entity.setIsDeleted(0);
        Permission saved = repository.save(entity);
        return mapper.toDTO(saved);
    }

    @Override
    public Optional<PermissionDTO> getPermissionById(Long id) {
        return repository.findById(id).map(mapper::toDTO).filter(e -> e.getIsDeleted() == 0);
    }

    @Override
    public PermissionDTO  getPermissionByCode(String code) {
        return mapper.toDTO(repository.findByCode(code));
    }

    @Override
    public List<PermissionDTO> getAllPermissions() {
        return mapper.toDTOList(repository.findAll().stream().filter(e -> e.getIsDeleted() == 0).toList());
    }

    @Override
    public void checkTenantRegisterPermission(Long tenantId, String operator) {
        // 可自定义黑名单、停用租户等逻辑
        boolean enabled = repository.isTenantEnabled(tenantId);
        if (!enabled) {
            throw new RuntimeException("该租户无权注册作业");
        }
    }

    @Override
    public boolean userHasPermission(Long userId, String permissionCode) {
        // 一般平台级权限直接查用户-权限表，具体可根据实际模型实现
        return repository.userHasPermission(userId, permissionCode) > 0;
    }

    @Override
    public boolean softDelete(Long id) {
        Optional<PermissionDTO> opt = repository.findById(id).map(mapper::toDTO).filter(e -> e.getIsDeleted() == 0);
        if (opt.isPresent()) {
            PermissionDTO dto = opt.get();
            dto.setIsDeleted(1);
            repository.save(mapper.toEntity(dto));
            return true;
        }
        return false;
    }
}
