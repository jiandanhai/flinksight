package com.flinksight.backend.service;

import com.flinksight.backend.domain.UserRole;
import com.flinksight.backend.mapper.UserPostStructMapper;
import com.flinksight.backend.mapper.UserRoleStructMapper;
import com.flinksight.backend.repository.UserRoleRepository;
import com.flinksight.common.dto.UserPostDTO;
import com.flinksight.common.dto.UserRoleDTO;
import com.flinksight.common.service.UserRoleService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional
public class UserRoleServiceImpl implements UserRoleService {
    private final UserRoleRepository repository;
    private final UserRoleStructMapper mapper;

    @Override
    public UserRoleDTO assignRoleToUser(Long userId, Long roleId, Long tenantId) {
        UserRole userRole = UserRole.builder()
            .userId(userId)
            .roleId(roleId)
            .tenantId(tenantId)
            .assignTime(LocalDateTime.now())
            .isDeleted(0)
            .build();
        return mapper.toDTO( repository.save(userRole));
    }

    @Override
    public boolean removeRoleFromUser(Long userId, Long roleId) {
        List<UserRole> list = repository.findByUserIdAndIsDeleted(userId, 0);
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
    public List<UserRoleDTO> findRolesByUserId(Long userId) {
        return mapper.toDTOList(repository.findByUserIdAndIsDeleted(userId, 0));
    }

    @Override
    public List<UserRoleDTO> findUsersByRoleId(Long roleId) {
        return mapper.toDTOList(repository.findByRoleIdAndIsDeleted(roleId, 0));
    }

    @Override
    public List<UserRoleDTO> findByTenantId(Long tenantId) {
        return mapper.toDTOList(repository.findByTenantIdAndIsDeleted(tenantId, 0));
    }

    @Override
    public Optional<UserRoleDTO> getById(Long id) {
        return repository.findById(id).map(mapper::toDTO).filter(e -> e.getIsDeleted() == 0);
    }

    @Override
    public boolean softDelete(Long id) {
        Optional<UserRoleDTO> opt = repository.findById(id).map(mapper::toDTO).filter(e -> e.getIsDeleted() == 0);
        if (opt.isPresent()) {
            UserRoleDTO dto = opt.get();
            dto.setIsDeleted(1);
            repository.save(mapper.toEntity(dto));
            return true;
        }
        return false;
    }
}
