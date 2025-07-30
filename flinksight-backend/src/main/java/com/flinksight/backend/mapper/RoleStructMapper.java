package com.flinksight.backend.mapper;

import org.mapstruct.Mapper;
import com.flinksight.backend.domain.Role;
import com.flinksight.common.dto.RoleDTO;

@Mapper(config = BaseStructMapperConfig.class)
public interface RoleStructMapper extends GenericMapper<RoleDTO, Role> {}
