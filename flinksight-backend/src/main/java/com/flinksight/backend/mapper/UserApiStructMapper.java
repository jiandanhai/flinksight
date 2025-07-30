package com.flinksight.backend.mapper;

import org.mapstruct.Mapper;
import com.flinksight.backend.domain.UserApi;
import com.flinksight.common.dto.UserApiDTO;

@Mapper(config = BaseStructMapperConfig.class)
public interface UserApiStructMapper extends GenericMapper<UserApiDTO, UserApi> {}
