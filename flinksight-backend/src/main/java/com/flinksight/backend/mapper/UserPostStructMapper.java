package com.flinksight.backend.mapper;

import com.flinksight.backend.domain.UserPost;
import com.flinksight.common.dto.UserPostDTO;
import org.mapstruct.Mapper;

@Mapper(config = BaseStructMapperConfig.class, componentModel = "spring")
public interface UserPostStructMapper extends GenericMapper<UserPostDTO, UserPost> {}
