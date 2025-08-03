package com.flinksight.backend.mapper;

import com.flinksight.backend.domain.RoleMenu;
import com.flinksight.common.dto.RoleMenuDTO;
import org.mapstruct.Mapper;

@Mapper(config = BaseStructMapperConfig.class, componentModel = "spring")
public interface RoleMenuStructMapper extends GenericMapper<RoleMenuDTO, RoleMenu> {}
