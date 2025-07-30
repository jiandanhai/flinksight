package com.flinksight.backend.mapper;

import org.mapstruct.Mapper;
import com.flinksight.backend.domain.IntegrationConfig;
import com.flinksight.common.dto.IntegrationConfigDTO;

@Mapper(config = BaseStructMapperConfig.class)
public interface IntegrationConfigStructMapper extends GenericMapper<IntegrationConfigDTO, IntegrationConfig> {}
