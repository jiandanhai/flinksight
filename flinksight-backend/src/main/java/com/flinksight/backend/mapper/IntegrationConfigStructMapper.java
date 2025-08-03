package com.flinksight.backend.mapper;

import com.flinksight.backend.domain.IntegrationConfig;
import com.flinksight.common.dto.IntegrationConfigDTO;
import org.mapstruct.Mapper;

@Mapper(config = BaseStructMapperConfig.class, componentModel = "spring")
public interface IntegrationConfigStructMapper extends GenericMapper<IntegrationConfigDTO, IntegrationConfig> {}
