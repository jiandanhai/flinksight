package com.flinksight.backend.mapper;

import com.flinksight.backend.domain.GroupRole;
import com.flinksight.common.dto.GroupRoleDTO;
import org.mapstruct.Mapper;

@Mapper(config = BaseStructMapperConfig.class, componentModel = "spring")
public interface GroupRoleStructMapper extends GenericMapper<GroupRoleDTO, GroupRole> {}
