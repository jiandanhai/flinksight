package com.flinksight.backend.mapper;

import com.flinksight.backend.domain.SysParam;
import com.flinksight.common.dto.SysParamDTO;
import org.mapstruct.Mapper;

@Mapper(config = BaseStructMapperConfig.class, componentModel = "spring")
public interface SysParamStructMapper extends GenericMapper<SysParamDTO, SysParam> {}
