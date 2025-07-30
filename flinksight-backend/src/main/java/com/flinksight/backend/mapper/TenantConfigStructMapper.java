package com.flinksight.backend.mapper;

import org.mapstruct.Mapper;
import com.flinksight.backend.domain.TenantConfig;
import com.flinksight.common.dto.TenantConfigDTO;

@Mapper(config = BaseStructMapperConfig.class)
public interface TenantConfigStructMapper extends GenericMapper<TenantConfigDTO, TenantConfig> {}
