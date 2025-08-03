package com.flinksight.backend.mapper;

import com.flinksight.backend.domain.Role;
import com.flinksight.common.dto.RoleDTO;
import org.mapstruct.Mapper;

@Mapper(config = BaseStructMapperConfig.class, componentModel = "spring")
public interface RoleStructMapper extends GenericMapper<RoleDTO, Role> {}
