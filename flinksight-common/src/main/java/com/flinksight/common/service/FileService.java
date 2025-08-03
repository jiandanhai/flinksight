package com.flinksight.common.service;

import com.flinksight.common.dto.FileDTO;
import com.flinksight.common.model.PageResult;

import java.util.Optional;

public interface FileService extends SoftDeleteService<FileDTO, Long> {
    FileDTO createOrUpdate(FileDTO entity);
    Optional<FileDTO> getById(Long id);
    PageResult<FileDTO> findByTenantId(Long tenantId,int page, int size);
    PageResult<FileDTO> getAll(int page, int size);
}
