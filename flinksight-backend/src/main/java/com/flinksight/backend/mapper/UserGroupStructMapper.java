package com.flinksight.backend.mapper;

import org.mapstruct.Mapper;
import com.flinksight.backend.domain.UserGroup;
import com.flinksight.common.dto.UserGroupDTO;

@Mapper(config = BaseStructMapperConfig.class)
public interface UserGroupStructMapper extends GenericMapper<UserGroupDTO, UserGroup> {}
