package com.flinksight.backend.mapper;

import com.flinksight.backend.domain.UserTenant;
import com.flinksight.common.dto.UserTenantDTO;
import org.mapstruct.Mapper;

@Mapper(config = BaseStructMapperConfig.class, componentModel = "spring")
public interface UserTenantStructMapper extends GenericMapper<UserTenantDTO, UserTenant> {}
