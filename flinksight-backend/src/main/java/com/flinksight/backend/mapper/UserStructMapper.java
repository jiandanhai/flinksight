package com.flinksight.backend.mapper;

import com.flinksight.backend.domain.User;
import com.flinksight.common.dto.UserDTO;
import org.mapstruct.Mapper;

@Mapper(config = BaseStructMapperConfig.class, componentModel = "spring")
public interface UserStructMapper extends GenericMapper<UserDTO, User> {}
