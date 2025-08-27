package com.flinksight.backend.service;

import com.flinksight.backend.common.PageHelpers;
import com.flinksight.backend.domain.UserRole;
import com.flinksight.backend.mapper.UserRoleStructMapper;
import com.flinksight.backend.repository.UserRoleRepository;
import com.flinksight.backend.security.SecurityUtil;
import com.flinksight.common.dto.UserRoleDTO;
import com.flinksight.common.model.PageResult;
import com.flinksight.common.service.UserRoleService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
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
    public void assignRole(Long userId, Long roleId) {
        if (!repository.existsByTenantIdAndUserIdAndRoleIdAndIsDeleted(SecurityUtil.getCurrentTenantId(),userId, roleId, 0)) {
            UserRole userRole = UserRole.builder()
                    .userId(userId)
                    .roleId(roleId)
                    .isDeleted(0)
                    .assignTime(LocalDateTime.now())
                    .build();
            repository.save(userRole);
        }
    }

    @Override
    public UserRoleDTO assignRoleToUser(Long userId, Long roleId) {
        UserRole userRole = UserRole.builder()
            .userId(userId)
            .roleId(roleId)
            .tenantId(SecurityUtil.getCurrentTenantId())
            .assignTime(LocalDateTime.now())
            .isDeleted(0)
            .build();
        return userRoleStructMapper.toDTO( repository.save(userRole));
    }

    @Override
    public boolean removeRoleFromUser(Long userId, Long roleId) {
        List<UserRole> list = repository.findByTenantIdAndUserIdAndRoleIdAndIsDeleted(SecurityUtil.getCurrentTenantId(),userId, roleId,0);
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
    public PageResult<UserRoleDTO> list(Long userId, Long roleId, int page, int size) {
        PageRequest pr = PageHelpers.pageRequest(page, size, null, UserRole.class); // 统一 1→0
        Page<UserRole> result = repository.pageQuery(SecurityUtil.getCurrentTenantId(), userId, roleId, pr);
        return PageHelpers.toPageResult(result, userRoleStructMapper::toDTO, true); //
    }

    @Override
    public Optional<UserRoleDTO> getById(Long id) {
        return repository.findById(id).map(userRoleStructMapper::toDTO).filter(e -> e.getIsDeleted() == 0);
    }

    @Override
    public boolean sDelete(Long id) {
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
