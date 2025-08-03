package com.flinksight.backend.mapper;

import com.flinksight.backend.domain.TenantResource;
import com.flinksight.common.dto.TenantResourceDTO;
import org.mapstruct.Mapper;

@Mapper(config = BaseStructMapperConfig.class, componentModel = "spring")
public interface TenantResourceStructMapper extends GenericMapper<TenantResourceDTO, TenantResource> {}
