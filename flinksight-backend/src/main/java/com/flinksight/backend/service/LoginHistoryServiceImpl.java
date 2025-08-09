package com.flinksight.backend.service;

import com.flinksight.backend.domain.LoginHistory;
import com.flinksight.backend.mapper.LoginHistoryStructMapper;
import com.flinksight.backend.repository.LoginHistoryRepository;
import com.flinksight.common.dto.LoginHistoryDTO;
import com.flinksight.common.model.PageResult;
import com.flinksight.common.service.LoginHistoryService;
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
public class LoginHistoryServiceImpl implements LoginHistoryService {
    private final LoginHistoryRepository repository;
    private final LoginHistoryStructMapper loginHistoryStructMapper;

    @Override
    public LoginHistoryDTO create(LoginHistoryDTO loginHistoryDTO) {
        LoginHistory entity = loginHistoryStructMapper.toEntity(loginHistoryDTO);
        entity.setIsDeleted(0);
        LoginHistory saved = repository.save(entity);
        return loginHistoryStructMapper.toDTO(saved);
    }

    @Override
    public Optional<LoginHistoryDTO> getById(Long id) {
        return repository.findById(id).map(loginHistoryStructMapper::toDTO).filter(e -> e.getIsDeleted() == 0);
    }

    @Override
    public PageResult<LoginHistoryDTO> findByUserId(Long userId,int page, int size) {
        Page<LoginHistory> result = repository.findByUserIdAndIsDeleted(userId,0, PageRequest.of(page, size, Sort.by("id").descending()));
        Page<LoginHistoryDTO> dtoPage = result.map(loginHistoryStructMapper::toDTO);
        return new PageResult<>(dtoPage);
    }

    @Override
    public PageResult<LoginHistoryDTO> findByTenantId(Long tenantId, int page, int size) {
        Page<LoginHistory> result = repository.findByUserIdAndIsDeletedOrderByLoginTimeDesc(tenantId,0, PageRequest.of(page, size, Sort.by("id").descending()));
        Page<LoginHistoryDTO> dtoPage = result.map(loginHistoryStructMapper::toDTO);
        return new PageResult<>(dtoPage);
    }

    @Override
    public long countUserSuccessLogin(Long userId) {
        return repository.countByUserIdAndSuccessFlagAndIsDeleted(userId, 1, 0);
    }

    @Override
    public boolean softDelete(Long id) {
        Optional<LoginHistoryDTO> opt = repository.findById(id).map(loginHistoryStructMapper::toDTO).filter(e -> e.getIsDeleted() == 0);
        if (opt.isPresent()) {
            LoginHistoryDTO dto = opt.get();
            dto.setIsDeleted(1);
            repository.save(loginHistoryStructMapper.toEntity(dto));
            return true;
        }
        return false;
    }
}
