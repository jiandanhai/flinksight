package com.flinksight.backend.mapper;

import org.mapstruct.Mapper;
import com.flinksight.backend.domain.UserRole;
import com.flinksight.common.dto.UserRoleDTO;

@Mapper(config = BaseStructMapperConfig.class)
public interface UserRoleStructMapper extends GenericMapper<UserRoleDTO, UserRole> {}
