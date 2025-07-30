package com.flinksight.backend.mapper;

import org.mapstruct.Mapper;
import com.flinksight.backend.domain.GroupRole;
import com.flinksight.common.dto.GroupRoleDTO;

@Mapper(config = BaseStructMapperConfig.class)
public interface GroupRoleStructMapper extends GenericMapper<GroupRoleDTO, GroupRole> {}
