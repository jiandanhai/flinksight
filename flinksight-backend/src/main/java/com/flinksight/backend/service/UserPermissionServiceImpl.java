package com.flinksight.backend.service;

import com.flinksight.backend.domain.UserPermission;
import com.flinksight.backend.mapper.UserPermissionStructMapper;
import com.flinksight.backend.repository.UserPermissionRepository;
import com.flinksight.common.dto.UserPermissionDTO;
import com.flinksight.common.model.PageResult;
import com.flinksight.common.service.UserPermissionService;
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
public class UserPermissionServiceImpl implements UserPermissionService {
    private final UserPermissionRepository repository;
    private final UserPermissionStructMapper userPermissionStructMapper;

    @Override
    public UserPermissionDTO assignPermissionToUser(Long userId, Long permissionId) {
        UserPermission up = UserPermission.builder()
            .userId(userId)
            .permissionId(permissionId)
            .isDeleted(0)
            .build();
        return userPermissionStructMapper.toDTO( repository.save(up));
    }

    @Override
    public boolean removePermissionFromUser(Long userId, Long permissionId) {
        List<UserPermission> list = repository.findByUserIdAndPermissionIdAndIsDeleted(userId,permissionId, 0);
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
    public PageResult<UserPermissionDTO> findByUserId(Long userId,int page, int size) {
        Page<UserPermission> result = repository.findByUserIdAndIsDeleted(userId,0, PageRequest.of(page, size, Sort.by("id").descending()));
        Page<UserPermissionDTO> dtoPage = result.map(userPermissionStructMapper::toDTO);
        return new PageResult<>(dtoPage);
    }

    @Override
    public PageResult<UserPermissionDTO> findByPermissionId(Long permissionId,int page, int size) {
        Page<UserPermission> result = repository.findByPermissionIdAndIsDeleted(permissionId,0, PageRequest.of(page, size, Sort.by("id").descending()));
        Page<UserPermissionDTO> dtoPage = result.map(userPermissionStructMapper::toDTO);
        return new PageResult<>(dtoPage);
    }

    @Override
    public Optional<UserPermissionDTO> getById(Long id) {
        return repository.findById(id).map(userPermissionStructMapper::toDTO).filter(e -> e.getIsDeleted() == 0);
    }

    @Override
    public boolean softDelete(Long id) {
        Optional<UserPermissionDTO> opt = repository.findById(id).map(userPermissionStructMapper::toDTO).filter(e -> e.getIsDeleted() == 0);
        if (opt.isPresent()) {
            UserPermissionDTO dto = opt.get();
            dto.setIsDeleted(1);
            repository.save(userPermissionStructMapper.toEntity(dto));
            return true;
        }
        return false;
    }
}
