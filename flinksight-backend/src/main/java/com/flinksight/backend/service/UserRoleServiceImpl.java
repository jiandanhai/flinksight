package com.flinksight.backend.service;

import com.flinksight.backend.domain.UserRole;
import com.flinksight.backend.mapper.UserRoleStructMapper;
import com.flinksight.backend.repository.UserRoleRepository;
import com.flinksight.common.dto.UserRoleDTO;
import com.flinksight.common.model.PageResult;
import com.flinksight.common.service.UserRoleService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional
public class UserRoleServiceImpl implements UserRoleService {
    private final UserRoleRepository repository;
    private final UserRoleStructMapper userRoleStructMapper;

    @Override
    public UserRoleDTO assignRoleToUser(Long userId, Long roleId, Long tenantId) {
        UserRole userRole = UserRole.builder()
            .userId(userId)
            .roleId(roleId)
            .tenantId(tenantId)
            .assignTime(LocalDateTime.now())
            .isDeleted(0)
            .build();
        return userRoleStructMapper.toDTO( repository.save(userRole));
    }

    @Override
    public boolean removeRoleFromUser(Long userId, Long roleId) {
        List<UserRole> list = repository.findByUserIdAndRoleIdAndIsDeleted(userId, roleId,0);
        for (UserRole ur : list) {
            if (ur.getRoleId().equals(roleId)) {
                ur.setIsDeleted(1);
                repository.save(ur);
                return true;
            }
        }
        return false;
    }

    @Override
    public PageResult<UserRoleDTO> findRolesByUserId(Long userId,int page, int size) {
        Page<UserRole> result = repository.findByUserIdAndIsDeleted(userId,0, PageRequest.of(page, size, Sort.by("id").descending()));
        Page<UserRoleDTO> dtoPage = result.map(userRoleStructMapper::toDTO);
        return new PageResult<>(dtoPage);
    }

    @Override
    public PageResult<UserRoleDTO> findUsersByRoleId(Long roleId,int page, int size) {
        Page<UserRole> result = repository.findByRoleIdAndIsDeleted(roleId,0, PageRequest.of(page, size, Sort.by("id").descending()));
        Page<UserRoleDTO> dtoPage = result.map(userRoleStructMapper::toDTO);
        return new PageResult<>(dtoPage);
    }

    @Override
    public PageResult<UserRoleDTO> findByTenantId(Long tenantId,int page, int size) {
        Page<UserRole> result = repository.findByTenantIdAndIsDeleted(tenantId,0, PageRequest.of(page, size, Sort.by("id").descending()));
        Page<UserRoleDTO> dtoPage = result.map(userRoleStructMapper::toDTO);
        return new PageResult<>(dtoPage);
    }

    @Override
    public Optional<UserRoleDTO> getById(Long id) {
        return repository.findById(id).map(userRoleStructMapper::toDTO).filter(e -> e.getIsDeleted() == 0);
    }

    @Override
    public boolean softDelete(Long id) {
        Optional<UserRoleDTO> opt = repository.findById(id).map(userRoleStructMapper::toDTO).filter(e -> e.getIsDeleted() == 0);
        if (opt.isPresent()) {
            UserRoleDTO dto = opt.get();
            dto.setIsDeleted(1);
            repository.save(userRoleStructMapper.toEntity(dto));
            return true;
        }
        return false;
    }
}
