package com.flinksight.backend.service;

import com.flinksight.backend.domain.OpsTask;
import com.flinksight.backend.mapper.OpsTaskMapper;
import com.flinksight.backend.repository.OpsTaskRepository;
import com.flinksight.common.dto.OpsTaskDTO;
import com.flinksight.common.model.PageResult;
import com.flinksight.common.service.OpsTaskService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

/**
 * 运维自动化任务业务实现类
 * 负责运维任务的核心业务逻辑
 */
@Service
@RequiredArgsConstructor
public class OpsTaskServiceImpl implements OpsTaskService {

    private final OpsTaskRepository opsTaskRepository;
    private final OpsTaskMapper opsTaskMapper;

    @Override
    @Transactional
    public OpsTaskDTO create(OpsTaskDTO dto) {
        OpsTask entity = opsTaskMapper.toEntity(dto);
        entity.setIsDeleted(0);
        OpsTask saved = opsTaskRepository.save(entity);
        return opsTaskMapper.toDTO(saved);
    }

    @Override
    @Transactional
    public OpsTaskDTO update(OpsTaskDTO dto) {
        OpsTask entity = opsTaskRepository.findById(dto.getId())
            .orElseThrow(() -> new IllegalArgumentException("任务不存在"));
        OpsTask saved = opsTaskRepository.save(entity);
        return opsTaskMapper.toDTO(saved);
    }

    @Override
    @Transactional
    public void delete(Long id) {
        OpsTask entity = opsTaskRepository.findById(id)
            .orElseThrow(() -> new IllegalArgumentException("任务不存在"));
        entity.setIsDeleted(1);
        opsTaskRepository.save(entity);
    }

    @Override
    @Transactional(readOnly = true)
    public OpsTaskDTO getById(Long id) {
        return opsTaskRepository.findById(id)
            .filter(e -> e.getIsDeleted() == 0)
            .map(opsTaskMapper::toDTO)
            .orElseThrow(() -> new IllegalArgumentException("任务不存在或已删除"));
    }

    @Override
    @Transactional(readOnly = true)
    public PageResult<OpsTaskDTO> listByTenant(Long tenantId, int page, int size) {
        Page<OpsTask> result = opsTaskRepository.findByTenantIdAndIsDeleted(tenantId, 0,  PageRequest.of(page, size, Sort.by("id").descending()));
        Page<OpsTaskDTO> dtoPage = result.map(opsTaskMapper::toDTO);
        return new PageResult<>(dtoPage);
    }

    @Override
    @Transactional(readOnly = true)
    public PageResult<OpsTaskDTO> listByTenantAndStatus(Long tenantId, String status, int page, int size) {
        Page<OpsTask> result = opsTaskRepository.findByTenantIdAndStatusAndIsDeleted(tenantId,status, 0,  PageRequest.of(page, size, Sort.by("id").descending()));
        Page<OpsTaskDTO> dtoPage = result.map(opsTaskMapper::toDTO);
        return new PageResult<>(dtoPage);
    }

    @Override
    public boolean softDelete(Long id) {
        Optional<OpsTaskDTO> opt = opsTaskRepository.findById(id).map(opsTaskMapper::toDTO).filter(e -> e.getIsDeleted() == 0);
        if (opt.isPresent()) {
            OpsTaskDTO dto = opt.get();
            dto.setIsDeleted(1);
            opsTaskRepository.save(opsTaskMapper.toEntity(dto));
            return true;
        }
        return false;
    }
}
