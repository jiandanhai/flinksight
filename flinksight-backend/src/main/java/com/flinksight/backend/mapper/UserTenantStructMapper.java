package com.flinksight.backend.mapper;

import org.mapstruct.Mapper;
import com.flinksight.backend.domain.UserTenant;
import com.flinksight.common.dto.UserTenantDTO;

@Mapper(config = BaseStructMapperConfig.class)
public interface UserTenantStructMapper extends GenericMapper<UserTenantDTO, UserTenant> {}
