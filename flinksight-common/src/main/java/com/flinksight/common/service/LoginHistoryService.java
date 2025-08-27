package com.flinksight.common.service;

import com.flinksight.common.dto.LoginHistoryDTO;
import com.flinksight.common.model.PageResult;

import java.util.Optional;

public interface LoginHistoryService extends SoftDeleteService<LoginHistoryDTO, Long> {
    LoginHistoryDTO create(LoginHistoryDTO entity);
    Optional<LoginHistoryDTO> getById(Long id);
    PageResult<LoginHistoryDTO> list(Long userId,int page, int size);

    long countUserSuccessLogin(Long userId);
}


