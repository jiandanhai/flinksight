package com.flinksight.backend.mapper;

import com.flinksight.backend.domain.TenantConfig;
import com.flinksight.common.dto.TenantConfigDTO;
import org.mapstruct.Mapper;

@Mapper(config = BaseStructMapperConfig.class, componentModel = "spring")
public interface TenantConfigStructMapper extends GenericMapper<TenantConfigDTO, TenantConfig> {}
