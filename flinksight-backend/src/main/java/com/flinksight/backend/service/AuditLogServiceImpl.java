package com.flinksight.backend.service;

import com.flinksight.backend.domain.ApiKey;
import com.flinksight.backend.domain.ApiWhitelist;
import com.flinksight.backend.domain.AuditLog;
import com.flinksight.backend.mapper.ApiWhitelistStructMapper;
import com.flinksight.backend.mapper.AuditLogStructMapper;
import com.flinksight.backend.repository.AuditLogRepository;
import com.flinksight.backend.security.tenant.TenantRequired;
import com.flinksight.common.dto.ApiKeyDTO;
import com.flinksight.common.dto.AuditLogDTO;
import com.flinksight.common.service.AuditLogService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

/**
 * 审计日志业务实现
 * AuditLog Service Impl
 */
@Service
@RequiredArgsConstructor
@Transactional
@TenantRequired
public class AuditLogServiceImpl implements AuditLogService{

    private final AuditLogRepository repository;
    private final AuditLogStructMapper mapper;

    @Override
    public AuditLogDTO createAuditLog(AuditLogDTO auditLogDTO) {
        AuditLog entity = mapper.toEntity(auditLogDTO);
        AuditLog saved = repository.save(entity);
        entity.setIsDeleted(0);
        return mapper.toDTO(saved);
    }

    @Override
    public Optional<AuditLogDTO> getAuditLogById(Long id) {
        return repository.findById(id).map(mapper::toDTO).filter(e -> e.getIsDeleted() == 0);
    }

    @Override
    public List<AuditLogDTO> getLogsByTenantAndUser(Long tenantId, Long userId) {
        return mapper.toDTOList(repository.findByTenantIdAndUserId(tenantId,userId));
    }

    @Override
    public boolean softDelete(Long id) {
        Optional<AuditLogDTO> opt = repository.findById(id).map(mapper::toDTO).filter(e -> e.getIsDeleted() == 0);
        if (opt.isPresent()) {
            AuditLogDTO dto = opt.get();
            dto.setIsDeleted(1);
            AuditLog entity = mapper.toEntity(dto);
            repository.save(entity);
            return true;
        }
        return false;
    }

    @Override
    public void logConfigChange(String configType, String dataId, Long tenantId, String operator, String config, String traceId) {
        AuditLog log = AuditLog.builder()
                .action("CONFIG_CHANGE")
                .content(config)
                .targetType(configType)
                .targetId(Long.valueOf(dataId))
                .tenantId(tenantId)
                .operator(operator)
                .traceId(traceId)
                .createdAt(LocalDateTime.now())
                .build();
        repository.save(log);
    }
}
