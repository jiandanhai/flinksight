package com.flinksight.common.service;

import com.flinksight.common.dto.LoginHistoryDTO;

import java.util.List;
import java.util.Optional;

public interface LoginHistoryService extends SoftDeleteService<LoginHistoryDTO, Long> {
    LoginHistoryDTO create(LoginHistoryDTO entity);
    Optional<LoginHistoryDTO> getById(Long id);
    List<LoginHistoryDTO> findByUserId(Long userId);
}
