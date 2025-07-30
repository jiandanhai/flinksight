package com.flinksight.backend.mapper;

import org.mapstruct.Mapper;
import com.flinksight.backend.domain.RoleMenu;
import com.flinksight.common.dto.RoleMenuDTO;

@Mapper(config = BaseStructMapperConfig.class)
public interface RoleMenuStructMapper extends GenericMapper<RoleMenuDTO, RoleMenu> {}
