package com.flinksight.backend.mapper;

import org.mapstruct.Mapper;
import com.flinksight.backend.domain.SysParam;
import com.flinksight.common.dto.SysParamDTO;

@Mapper(config = BaseStructMapperConfig.class)
public interface SysParamStructMapper extends GenericMapper<SysParamDTO, SysParam> {}
