package com.flinksight.backend.service;

import com.flinksight.backend.domain.UserPermission;
import com.flinksight.backend.mapper.UserGroupStructMapper;
import com.flinksight.backend.mapper.UserPermissionStructMapper;
import com.flinksight.backend.repository.UserPermissionRepository;
import com.flinksight.common.dto.UserGroupDTO;
import com.flinksight.common.dto.UserPermissionDTO;
import com.flinksight.common.service.UserPermissionService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional
public class UserPermissionServiceImpl implements UserPermissionService {
    private final UserPermissionRepository repository;
    private final UserPermissionStructMapper mapper;

    @Override
    public UserPermissionDTO assignPermissionToUser(Long userId, Long permissionId) {
        UserPermission up = UserPermission.builder()
            .userId(userId)
            .permissionId(permissionId)
            .isDeleted(0)
            .build();
        return mapper.toDTO( repository.save(up));
    }

    @Override
    public boolean removePermissionFromUser(Long userId, Long permissionId) {
        List<UserPermission> list = repository.findByUserIdAndIsDeleted(userId, 0);
        for (UserPermission up : list) {
            if (up.getPermissionId().equals(permissionId)) {
                up.setIsDeleted(1);
                repository.save(up);
                return true;
            }
        }
        return false;
    }

    @Override
    public List<UserPermissionDTO> findByUserId(Long userId) {
        return mapper.toDTOList(repository.findByUserIdAndIsDeleted(userId, 0));
    }

    @Override
    public List<UserPermissionDTO> findByPermissionId(Long permissionId) {
        return mapper.toDTOList(repository.findByPermissionIdAndIsDeleted(permissionId, 0));
    }

    @Override
    public Optional<UserPermissionDTO> getById(Long id) {
        return repository.findById(id).map(mapper::toDTO).filter(e -> e.getIsDeleted() == 0);
    }

    @Override
    public boolean softDelete(Long id) {
        Optional<UserPermissionDTO> opt = repository.findById(id).map(mapper::toDTO).filter(e -> e.getIsDeleted() == 0);
        if (opt.isPresent()) {
            UserPermissionDTO dto = opt.get();
            dto.setIsDeleted(1);
            repository.save(mapper.toEntity(dto));
            return true;
        }
        return false;
    }
}
