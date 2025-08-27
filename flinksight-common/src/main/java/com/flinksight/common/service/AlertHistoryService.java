package com.flinksight.common.service;

import com.flinksight.common.dto.AlertHistoryDTO;
import com.flinksight.common.model.PageResult;

import java.util.Optional;

public interface AlertHistoryService extends SoftDeleteService<AlertHistoryDTO, Long> {
    AlertHistoryDTO createOrUpdate(AlertHistoryDTO entity);
    Optional<AlertHistoryDTO> getById(Long id);
    PageResult<AlertHistoryDTO> list(int page, int size);
}
