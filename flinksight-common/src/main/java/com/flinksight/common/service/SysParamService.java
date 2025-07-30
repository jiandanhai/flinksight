package com.flinksight.common.service;

import com.flinksight.common.dto.SysParamDTO;

import java.util.List;
import java.util.Optional;

public interface SysParamService extends SoftDeleteService<SysParamDTO, Long> {
    SysParamDTO createOrUpdate(SysParamDTO entity);
    Optional<SysParamDTO> getById(Long id);
    List<SysParamDTO> getAll();
}
