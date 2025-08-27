package com.flinksight.backend.service;

import com.flinksight.backend.common.PageHelpers;
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
        // 直接软删除
        int updated = repository.softDeleteByUserIdAndTenantId(userId, tenantId);
        return updated > 0;
    }

    @Override
    public Optional<UserTenantDTO> getById(Long id) {
        return repository.findById(id).map(userTenantStructMapper::toDTO).filter(e -> e.getIsDeleted() == 0);
    }


    @Override
    public PageResult<UserTenantDTO> list(Long userId, Long tenantId, int page, int size) {
        PageRequest pr = PageHelpers.pageRequest(page, size, null, UserTenant.class); // 统一 1→0
        Page<UserTenant> result = repository.pageQuery(userId, tenantId, pr);
        return PageHelpers.toPageResult(result, userTenantStructMapper::toDTO, true); //
    }

    @Override
    public boolean sDelete(Long id) {
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
