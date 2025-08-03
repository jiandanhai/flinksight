package com.flinksight.backend.mapper;

import com.flinksight.backend.domain.UserRole;
import com.flinksight.common.dto.UserRoleDTO;
import org.mapstruct.Mapper;

@Mapper(config = BaseStructMapperConfig.class, componentModel = "spring")
public interface UserRoleStructMapper extends GenericMapper<UserRoleDTO, UserRole> {}
