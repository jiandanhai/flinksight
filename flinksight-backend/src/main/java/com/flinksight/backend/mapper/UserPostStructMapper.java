package com.flinksight.backend.mapper;

import org.mapstruct.Mapper;
import com.flinksight.backend.domain.UserPost;
import com.flinksight.common.dto.UserPostDTO;

@Mapper(config = BaseStructMapperConfig.class)
public interface UserPostStructMapper extends GenericMapper<UserPostDTO, UserPost> {}
