package com.flinksight.backend.mapper;

import com.flinksight.backend.domain.RoleDataScope;
import com.flinksight.common.dto.RoleDataScopeDTO;
import org.mapstruct.Mapper;

@Mapper(config = BaseStructMapperConfig.class, componentModel = "spring")
public interface RoleDataScopeStructMapper extends GenericMapper<RoleDataScopeDTO, RoleDataScope> {}
