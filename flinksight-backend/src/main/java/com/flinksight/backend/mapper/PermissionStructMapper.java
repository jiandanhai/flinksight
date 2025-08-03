package com.flinksight.backend.mapper;

import com.flinksight.backend.domain.Permission;
import com.flinksight.common.dto.PermissionDTO;
import org.mapstruct.Mapper;

@Mapper(config = BaseStructMapperConfig.class, componentModel = "spring")
public interface PermissionStructMapper extends GenericMapper<PermissionDTO, Permission> {}
