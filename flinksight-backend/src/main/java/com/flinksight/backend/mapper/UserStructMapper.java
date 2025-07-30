package com.flinksight.backend.mapper;

import org.mapstruct.Mapper;
import com.flinksight.backend.domain.User;
import com.flinksight.common.dto.UserDTO;

@Mapper(config = BaseStructMapperConfig.class)
public interface UserStructMapper extends GenericMapper<UserDTO, User> {}
