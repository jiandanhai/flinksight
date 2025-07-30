package com.flinksight.common.service;

import com.flinksight.common.dto.ApiKeyDTO;
import com.flinksight.common.dto.FileDTO;

import java.util.List;
import java.util.Optional;

public interface FileService extends SoftDeleteService<FileDTO, Long> {
    FileDTO createOrUpdate(FileDTO entity);
    Optional<FileDTO> getById(Long id);
    List<FileDTO> findByTenantId(Long tenantId);
    List<FileDTO> getAll();
}
