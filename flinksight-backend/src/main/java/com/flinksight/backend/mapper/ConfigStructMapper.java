package com.flinksight.backend.mapper;

import org.mapstruct.Mapper;
import com.flinksight.backend.domain.Config;
import com.flinksight.common.dto.ConfigDTO;

@Mapper(config = BaseStructMapperConfig.class)
public interface ConfigStructMapper extends GenericMapper<ConfigDTO, Config> {}
