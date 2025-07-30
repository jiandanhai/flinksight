package com.flinksight.backend.mapper;

import org.mapstruct.Mapper;
import com.flinksight.backend.domain.UserPermission;
import com.flinksight.common.dto.UserPermissionDTO;

@Mapper(config = BaseStructMapperConfig.class)
public interface UserPermissionStructMapper extends GenericMapper<UserPermissionDTO, UserPermission> {}
