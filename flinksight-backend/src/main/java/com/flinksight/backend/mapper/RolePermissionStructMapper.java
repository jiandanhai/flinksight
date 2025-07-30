package com.flinksight.backend.mapper;

import org.mapstruct.Mapper;
import com.flinksight.backend.domain.RolePermission;
import com.flinksight.common.dto.RolePermissionDTO;

@Mapper(config = BaseStructMapperConfig.class)
public interface RolePermissionStructMapper extends GenericMapper<RolePermissionDTO, RolePermission> {}
