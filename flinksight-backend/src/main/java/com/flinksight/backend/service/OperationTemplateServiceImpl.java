package com.flinksight.backend.service;

import com.flinksight.backend.common.PageHelpers;
import com.flinksight.backend.domain.OperationTemplate;
import com.flinksight.backend.exec.TemplateExecutor;
import com.flinksight.backend.exec.TemplateExecutorRegistry;
import com.flinksight.backend.mapper.OperationTemplateStructMapper;
import com.flinksight.backend.repository.OperationTemplateRepository;
import com.flinksight.backend.security.SecurityUtil;
import com.flinksight.backend.security.tenant.TenantContext;
import com.flinksight.common.dto.OperationTemplateDTO;
import com.flinksight.common.model.PageResult;
import com.flinksight.common.service.OperationTemplateService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;
import org.springframework.util.PropertyPlaceholderHelper;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class OperationTemplateServiceImpl implements OperationTemplateService {
    private final OperationTemplateRepository repository;
    private final TemplateExecutorRegistry executorRegistry;
    private final OperationTemplateStructMapper operationTemplateStructMapper;
    //变量渲染,支持 ${var} 占位符
    private final PropertyPlaceholderHelper placeholder =
            new PropertyPlaceholderHelper("${", "}");

    @Override
    @Transactional
    public OperationTemplateDTO create(OperationTemplateDTO dto) {
        Assert.hasText(dto.getName(), "name required");
        Assert.hasText(dto.getType(), "type required");
        Assert.hasText(dto.getContent(), "content required");

        // 唯一性校验：同租户 type+name 不重复
        if (repository.existsByTenantIdAndTypeAndNameAndIsDeleted(SecurityUtil.getCurrentTenantId(), dto.getType(), dto.getName(), 0)) {
            throw new DuplicateKeyException("Template already exists: " + dto.getType() + "/" + dto.getName());
        }

        OperationTemplate e = operationTemplateStructMapper.toEntity(dto);
        e.setTenantId(SecurityUtil.getCurrentTenantId());
        e.setCreateTime(LocalDateTime.now());
        e.setIsDeleted(0);
        return operationTemplateStructMapper.toDTO(repository.save(e));
    }

    @Override
    @Transactional
    public OperationTemplateDTO update(Long id, OperationTemplateDTO dto) {
        OperationTemplate e = repository.findById(id).orElseThrow(() -> notFound(id));
        assertTenant(e.getTenantId(), SecurityUtil.getCurrentTenantId());

        // 唯一性校验（排除自己）
        repository.findByTenantIdAndTypeAndNameAndIsDeletedAndIdNot(
                SecurityUtil.getCurrentTenantId(), dto.getType() == null ? e.getType() : dto.getType(),
                dto.getName() == null ? e.getName() : dto.getName(), 0, id
        ).ifPresent(conflict -> {
            throw new DuplicateKeyException("Template already exists: " + conflict.getType() + "/" + conflict.getName());
        });

        operationTemplateStructMapper.mergeIgnoreNullAndBlank(dto, e);
        return operationTemplateStructMapper.toDTO(repository.save(e));
    }

    @Override
    public OperationTemplateDTO getById(Long id) {
        OperationTemplate e = repository.findById(id).orElseThrow(() -> notFound(id));
        assertTenant(e.getTenantId(), SecurityUtil.getCurrentTenantId());
        return operationTemplateStructMapper.toDTO(e);
    }

    @Override
    public PageResult<OperationTemplateDTO> list(String type, String keyword, int page, int size) {
        PageRequest pr = PageHelpers.pageRequest(page, size, null, OperationTemplate.class); // 统一 1→0
        Page<OperationTemplate> result;
        if ((type == null || type.isBlank()) && (keyword == null || keyword.isBlank())) {
            result = repository.findByTenantIdAndIsDeleted(SecurityUtil.getCurrentTenantId(), 0, pr);
        } else {
            result = repository.findByTenantIdAndIsDeletedAndTypeContainingAndNameContaining(
                    SecurityUtil.getCurrentTenantId(), 0,
                    type == null ? "" : type,
                    keyword == null ? "" : keyword, pr);
        }
        return PageHelpers.toPageResult(result, operationTemplateStructMapper::toDTO, true); // 返回
    }


    @Override
    public boolean sDelete(Long id) {
        OperationTemplate e = repository.findById(id).orElseThrow(() -> notFound(id));
        assertTenant(e.getTenantId(), SecurityUtil.getCurrentTenantId());
        e.setIsDeleted(1);
        repository.save(e);
        return true;
    }

    @Override
    public String dryRun(Long id, Map<String, Object> vars) {
        OperationTemplate e = repository.findById(id).orElseThrow(() -> notFound(id));
        assertTenant(e.getTenantId(), SecurityUtil.getCurrentTenantId());
        String rendered = render(e.getContent(), enrichVars(vars));
        return rendered;
    }

    @Override
    public String execute(Long id, Map<String, Object> vars) {
        OperationTemplate e = repository.findById(id).orElseThrow(() -> notFound(id));
        assertTenant(e.getTenantId(), SecurityUtil.getCurrentTenantId());

        String rendered = render(e.getContent(), enrichVars(vars));
        TemplateExecutor executor = executorRegistry.resolve(e.getType());
        if (executor == null) {
            // 无匹配执行器时，采用 Noop 或直接报错
            executor = executorRegistry.resolve("NOOP");
            log.warn("No executor found for type={}, fallback to NOOP", e.getType());
        }
        try {
            return executor.execute(rendered, vars == null ? Map.of() : vars);
        } catch (Exception ex) {
            // 让异常抛出，@OpAudit 会记录 FAIL 并写入 failReason
            throw new RuntimeException("Execute template failed: " + ex.getMessage(), ex);
        }
    }

    /* ==================== 内部辅助 ==================== */

    private void assertTenant(Long entityTenant, Long currentTenant) {
        if (entityTenant == null || !entityTenant.equals(currentTenant)) {
            throw new SecurityException("Cross tenant access denied");
        }
    }

    private RuntimeException notFound(Long id) {
        return new IllegalArgumentException("OperationTemplate not found: " + id);
    }

    private String render(String tpl, Map<String, Object> vars) {
        if (tpl == null) return null;
        return placeholder.replacePlaceholders(tpl, name -> {
            Object v = vars.get(name);
            return v == null ? "" : String.valueOf(v);
        });
    }

    private Map<String, Object> enrichVars(Map<String, Object> vars) {
        Map<String, Object> map = new HashMap<>();
        if (vars != null) map.putAll(vars);
        map.putIfAbsent("tenantId", TenantContext.getTenantId());
        map.putIfAbsent("now", LocalDateTime.now().toString());
        return map;
    }
}
