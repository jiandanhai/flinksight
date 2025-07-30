package com.flinksight.backend.mapper;

import org.mapstruct.Mapper;
import com.flinksight.backend.domain.RoleDataScope;
import com.flinksight.common.dto.RoleDataScopeDTO;

@Mapper(config = BaseStructMapperConfig.class)
public interface RoleDataScopeStructMapper extends GenericMapper<RoleDataScopeDTO, RoleDataScope> {}
