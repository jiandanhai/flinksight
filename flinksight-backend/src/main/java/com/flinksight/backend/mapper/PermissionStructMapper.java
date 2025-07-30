package com.flinksight.backend.mapper;

import org.mapstruct.Mapper;
import com.flinksight.backend.domain.Permission;
import com.flinksight.common.dto.PermissionDTO;

@Mapper(config = BaseStructMapperConfig.class)
public interface PermissionStructMapper extends GenericMapper<PermissionDTO, Permission> {}
