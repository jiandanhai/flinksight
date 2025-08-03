package com.flinksight.backend.mapper;

import com.flinksight.backend.domain.Config;
import com.flinksight.common.dto.ConfigDTO;
import org.mapstruct.Mapper;

@Mapper(config = BaseStructMapperConfig.class, componentModel = "spring")
public interface ConfigStructMapper extends GenericMapper<ConfigDTO, Config> {}
