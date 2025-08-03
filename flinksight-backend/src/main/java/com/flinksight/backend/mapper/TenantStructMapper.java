package com.flinksight.backend.mapper;

import com.flinksight.backend.domain.Tenant;
import com.flinksight.common.dto.TenantDTO;
import org.mapstruct.Mapper;

@Mapper(config = BaseStructMapperConfig.class, componentModel = "spring")
public interface TenantStructMapper extends GenericMapper<TenantDTO, Tenant> {}
