package com.flinksight.backend.mapper;

import com.flinksight.backend.domain.RolePermission;
import com.flinksight.common.dto.RolePermissionDTO;
import org.mapstruct.Mapper;

@Mapper(config = BaseStructMapperConfig.class, componentModel = "spring")
public interface RolePermissionStructMapper extends GenericMapper<RolePermissionDTO, RolePermission> {}
