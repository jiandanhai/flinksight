package com.flinksight.backend.service;

import com.flinksight.backend.common.PageHelpers;
import com.flinksight.backend.domain.ApiAccessLog;
import com.flinksight.backend.mapper.ApiAccessLogStructMapper;
import com.flinksight.backend.repository.ApiAccessLogRepository;
import com.flinksight.backend.security.SecurityUtil;
import com.flinksight.common.dto.ApiAccessLogDTO;
import com.flinksight.common.model.PageResult;
import com.flinksight.common.service.ApiAccessLogService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional
public class ApiAccessLogServiceImpl implements ApiAccessLogService {
    private final ApiAccessLogRepository repository;
    private final ApiAccessLogStructMapper apiAccessLogStructMapper;

    @Override
    public ApiAccessLogDTO createOrUpdate(ApiAccessLogDTO apiAccessLogDTO) {
        ApiAccessLog entity = apiAccessLogStructMapper.toEntity(apiAccessLogDTO);
        ApiAccessLog saved = repository.save(entity);
        entity.setIsDeleted(0);
        return apiAccessLogStructMapper.toDTO(saved);
    }

    @Override
    public Optional<ApiAccessLogDTO> getById(Long id) {
        return repository.findById(id).map(apiAccessLogStructMapper::toDTO).filter(e -> e.getIsDeleted() == 0);
    }

    @Override
    public PageResult<ApiAccessLogDTO> list(int page, int size) {
        PageRequest pr = PageHelpers.pageRequest(page, size, null, ApiAccessLog.class); // 统一 1→0
        Page<ApiAccessLog> result = repository.findByTenantIdAndIsDeleted(SecurityUtil.getCurrentTenantId(),0,pr);
        return PageHelpers.toPageResult(result, apiAccessLogStructMapper::toDTO, true); // 返回 1-based
    }


    @Override
    public boolean sDelete(Long id) {
        Optional<ApiAccessLogDTO> opt = repository.findById(id).map(apiAccessLogStructMapper::toDTO).filter(e -> e.getIsDeleted() == 0);
        if (opt.isPresent()) {
            ApiAccessLogDTO dto = opt.get();
            dto.setIsDeleted(1);
            ApiAccessLog entity = apiAccessLogStructMapper.toEntity(dto);
            repository.save(entity);
            return true;
        }
        return false;
    }
}
