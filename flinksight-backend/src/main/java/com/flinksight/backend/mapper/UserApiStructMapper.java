package com.flinksight.backend.mapper;

import com.flinksight.backend.domain.UserApi;
import com.flinksight.common.dto.UserApiDTO;
import org.mapstruct.Mapper;

@Mapper(config = BaseStructMapperConfig.class, componentModel = "spring")
public interface UserApiStructMapper extends GenericMapper<UserApiDTO, UserApi> {}
