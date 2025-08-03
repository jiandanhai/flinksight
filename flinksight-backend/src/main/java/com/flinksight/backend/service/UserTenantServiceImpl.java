package com.flinksight.backend.service;

import com.flinksight.backend.domain.UserTenant;
import com.flinksight.backend.mapper.UserTenantStructMapper;
import com.flinksight.backend.repository.UserTenantRepository;
import com.flinksight.common.dto.UserTenantDTO;
import com.flinksight.common.model.PageResult;
import com.flinksight.common.service.UserTenantService;
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
public class UserTenantServiceImpl implements UserTenantService {
    private final UserTenantRepository repository;
    private final UserTenantStructMapper userTenantStructMapper;

    @Override
    public UserTenantDTO assignTenantToUser(Long userId, Long tenantId) {
        UserTenant ut = UserTenant.builder()
            .userId(userId)
            .tenantId(tenantId)
            .isDeleted(0)
            .build();
        return userTenantStructMapper.toDTO(repository.save(ut));
    }

    @Override
    public boolean removeTenantFromUser(Long userId, Long tenantId) {
        List<UserTenant> list = repository.findByUserIdAndTenantIdAndIsDeleted(userId, tenantId,0);
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
    public PageResult<UserTenantDTO> findByUserId(Long userId,int page, int size) {
        Page<UserTenant> result = repository.findByUserIdAndIsDeleted(userId,0, PageRequest.of(page, size, Sort.by("id").descending()));
        Page<UserTenantDTO> dtoPage = result.map(userTenantStructMapper::toDTO);
        return new PageResult<>(dtoPage);
    }

    @Override
    public PageResult<UserTenantDTO> findByTenantId(Long tenantId,int page, int size) {
        Page<UserTenant> result = repository.findByTenantIdAndIsDeleted(tenantId,0, PageRequest.of(page, size, Sort.by("id").descending()));
        Page<UserTenantDTO> dtoPage = result.map(userTenantStructMapper::toDTO);
        return new PageResult<>(dtoPage);
    }

    @Override
    public Optional<UserTenantDTO> getById(Long id) {
        return repository.findById(id).map(userTenantStructMapper::toDTO).filter(e -> e.getIsDeleted() == 0);
    }

    @Override
    public boolean softDelete(Long id) {
        Optional<UserTenantDTO> opt = repository.findById(id).map(userTenantStructMapper::toDTO).filter(e -> e.getIsDeleted() == 0);
        if (opt.isPresent()) {
            UserTenantDTO dto = opt.get();
            dto.setIsDeleted(1);
            repository.save(userTenantStructMapper.toEntity(dto));
            return true;
        }
        return false;
    }
}
