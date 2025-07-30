package com.flinksight.backend.mapper;

import org.mapstruct.Mapper;
import com.flinksight.backend.domain.TenantResource;
import com.flinksight.common.dto.TenantResourceDTO;

@Mapper(config = BaseStructMapperConfig.class)
public interface TenantResourceStructMapper extends GenericMapper<TenantResourceDTO, TenantResource> {}
