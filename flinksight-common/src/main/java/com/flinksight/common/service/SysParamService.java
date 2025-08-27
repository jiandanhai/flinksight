package com.flinksight.common.service;

import com.flinksight.common.dto.SysParamDTO;
import com.flinksight.common.model.PageResult;

import java.util.Optional;

public interface SysParamService extends SoftDeleteService<SysParamDTO, Long> {
    SysParamDTO createOrUpdate(SysParamDTO entity);
    Optional<SysParamDTO> getById(Long id);
    PageResult<SysParamDTO> list(int page, int size);
}
