package com.flinksight.backend.mapper;

import com.flinksight.backend.domain.UserPermission;
import com.flinksight.common.dto.UserPermissionDTO;
import org.mapstruct.Mapper;

@Mapper(config = BaseStructMapperConfig.class, componentModel = "spring")
public interface UserPermissionStructMapper extends GenericMapper<UserPermissionDTO, UserPermission> {}
