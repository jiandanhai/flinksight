package com.flinksight.backend.mapper;

import org.mapstruct.Mapper;
import com.flinksight.backend.domain.Tenant;
import com.flinksight.common.dto.TenantDTO;

@Mapper(config = BaseStructMapperConfig.class)
public interface TenantStructMapper extends GenericMapper<TenantDTO, Tenant> {}
