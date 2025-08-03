package com.flinksight.backend.service;

import com.flinksight.backend.domain.Tenant;
import com.flinksight.backend.exception.BusinessException;
import com.flinksight.backend.mapper.TenantStructMapper;
import com.flinksight.backend.repository.TenantRepository;
import com.flinksight.backend.security.tenant.TenantRequired;
import com.flinksight.common.dto.TenantDTO;
import com.flinksight.common.enums.ErrorCode;
import com.flinksight.common.model.PageResult;
import com.flinksight.common.service.TenantService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.Optional;

/**
 * 租户业务实现
 * Tenant Service Impl
 */
@Service
@RequiredArgsConstructor
@Transactional
@TenantRequired
public class TenantServiceImpl implements TenantService {

    private final TenantRepository repository;
    private final TenantStructMapper tenantStructMapper;

    @Override
    public TenantDTO createTenant(TenantDTO tenantDTO) {
        Tenant entity = tenantStructMapper.toEntity(tenantDTO);
        entity.setIsDeleted(0);
        Tenant saved = repository.save(entity);
        return tenantStructMapper.toDTO(saved);
    }

    @Override
    public Optional<TenantDTO> getTenantById(Long tenantId) {
        return repository.findById(tenantId).map(tenantStructMapper::toDTO).filter(e -> e.getIsDeleted() == 0);
    }

    @Override
    public Optional<TenantDTO> getTenantByCode(String code) {
        return repository.findByCode(code).map(tenantStructMapper::toDTO).filter(e -> e.getIsDeleted() == 0);
    }

    @Override
    public PageResult<TenantDTO> getAllTenants(int page, int size) {
        Page<Tenant> result = repository.findByIsDeleted(0, PageRequest.of(page, size, Sort.by("id").descending()));
        Page<TenantDTO> dtoPage = result.map(tenantStructMapper::toDTO);
        return new PageResult<>(dtoPage);
    }

    @Override
    public TenantDTO updateTenant(TenantDTO tenantDTO) {
        Optional<TenantDTO> opt = repository.findById(tenantDTO.getId()).map(tenantStructMapper::toDTO).filter(e -> e.getIsDeleted() == 0);
        Tenant entity = tenantStructMapper.toEntity(tenantDTO);
        if(opt.isPresent()) {
            TenantDTO t = opt.get();
            entity.setContact(t.getContact());
            entity.setStatus(t.getStatus());
            entity.setIsDeleted(0);
            // 可扩展其它字段
            return tenantStructMapper.toDTO(repository.save(entity));
        }
        throw new BusinessException(ErrorCode.NOT_FOUND, "租户不存在");
    }

    @Override
    public boolean softDelete(Long id) {
        Optional<TenantDTO> opt = repository.findById(id).map(tenantStructMapper::toDTO).filter(e -> e.getIsDeleted() == 0);
        if (opt.isPresent()) {
            TenantDTO dto = opt.get();
            dto.setIsDeleted(1);
            repository.save(tenantStructMapper.toEntity(dto));
            return true;
        }
        return false;
    }
}
