package com.flinksight.backend.mapper;

import com.flinksight.backend.domain.UserGroup;
import com.flinksight.common.dto.UserGroupDTO;
import org.mapstruct.Mapper;

@Mapper(config = BaseStructMapperConfig.class, componentModel = "spring")
public interface UserGroupStructMapper extends GenericMapper<UserGroupDTO, UserGroup> {}
