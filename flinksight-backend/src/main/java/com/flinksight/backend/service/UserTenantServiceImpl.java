package com.flinksight.backend.service;

import com.flinksight.backend.domain.UserTenant;
import com.flinksight.backend.mapper.UserStructMapper;
import com.flinksight.backend.mapper.UserTenantStructMapper;
import com.flinksight.backend.repository.UserTenantRepository;
import com.flinksight.common.dto.UserPermissionDTO;
import com.flinksight.common.dto.UserTenantDTO;
import com.flinksight.common.service.UserTenantService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional
public class UserTenantServiceImpl implements UserTenantService {
    private final UserTenantRepository repository;
    private final UserTenantStructMapper mapper;

    @Override
    public UserTenantDTO assignTenantToUser(Long userId, Long tenantId) {
        UserTenant ut = UserTenant.builder()
            .userId(userId)
            .tenantId(tenantId)
            .isDeleted(0)
            .build();
        return mapper.toDTO(repository.save(ut));
    }

    @Override
    public boolean removeTenantFromUser(Long userId, Long tenantId) {
        List<UserTenant> list = repository.findByUserIdAndIsDeleted(userId, 0);
        for (UserTenant ut : list) {
            if (ut.getTenantId().equals(tenantId)) {
                ut.setIsDeleted(1);
                repository.save(ut);
                return true;
            }
        }
        return false;
    }

    @Override
    public List<UserTenantDTO> findByUserId(Long userId) {
        return mapper.toDTOList(repository.findByUserIdAndIsDeleted(userId, 0));
    }

    @Override
    public List<UserTenantDTO> findByTenantId(Long tenantId) {
        return mapper.toDTOList(repository.findByTenantIdAndIsDeleted(tenantId, 0));
    }

    @Override
    public Optional<UserTenantDTO> getById(Long id) {
        return repository.findById(id).map(mapper::toDTO).filter(e -> e.getIsDeleted() == 0);
    }

    @Override
    public boolean softDelete(Long id) {
        Optional<UserTenantDTO> opt = repository.findById(id).map(mapper::toDTO).filter(e -> e.getIsDeleted() == 0);
        if (opt.isPresent()) {
            UserTenantDTO dto = opt.get();
            dto.setIsDeleted(1);
            repository.save(mapper.toEntity(dto));
            return true;
        }
        return false;
    }
}
