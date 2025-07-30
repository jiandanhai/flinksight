package com.flinksight.backend.service;

import com.flinksight.backend.domain.Tag;
import com.flinksight.backend.domain.Tenant;
import com.flinksight.backend.exception.BusinessException;
import com.flinksight.backend.mapper.TenantResourceStructMapper;
import com.flinksight.backend.mapper.TenantStructMapper;
import com.flinksight.backend.repository.TenantRepository;
import com.flinksight.backend.security.tenant.TenantRequired;
import com.flinksight.common.dto.TenantDTO;
import com.flinksight.common.dto.TenantResourceDTO;
import com.flinksight.common.service.TenantService;
import com.flinksight.common.enums.ErrorCode;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
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
    private final TenantStructMapper mapper;

    @Override
    public TenantDTO createTenant(TenantDTO tenantDTO) {
        Tenant entity = mapper.toEntity(tenantDTO);
        entity.setIsDeleted(0);
        Tenant saved = repository.save(entity);
        return mapper.toDTO(saved);
    }

    @Override
    public Optional<TenantDTO> getTenantById(Long tenantId) {
        return repository.findById(tenantId).map(mapper::toDTO).filter(e -> e.getIsDeleted() == 0);
    }

    @Override
    public Optional<TenantDTO> getTenantByCode(String code) {
        return repository.findByCode(code).map(mapper::toDTO).filter(e -> e.getIsDeleted() == 0);
    }

    @Override
    public List<TenantDTO> getAllTenants() {
        return mapper.toDTOList(repository.findAll().stream().filter(e -> e.getIsDeleted() == 0).toList());
    }

    @Override
    public TenantDTO updateTenant(TenantDTO tenantDTO) {
        Optional<TenantDTO> opt = repository.findById(tenantDTO.getId()).map(mapper::toDTO).filter(e -> e.getIsDeleted() == 0);
        Tenant entity = mapper.toEntity(tenantDTO);
        if(opt.isPresent()) {
            TenantDTO t = opt.get();
            entity.setContact(t.getContact());
            entity.setStatus(t.getStatus());
            entity.setIsDeleted(0);
            // 可扩展其它字段
            return mapper.toDTO(repository.save(entity));
        }
        throw new BusinessException(ErrorCode.NOT_FOUND, "租户不存在");
    }

    @Override
    public boolean softDelete(Long id) {
        Optional<TenantDTO> opt = repository.findById(id).map(mapper::toDTO).filter(e -> e.getIsDeleted() == 0);
        if (opt.isPresent()) {
            TenantDTO dto = opt.get();
            dto.setIsDeleted(1);
            repository.save(mapper.toEntity(dto));
            return true;
        }
        return false;
    }
}
