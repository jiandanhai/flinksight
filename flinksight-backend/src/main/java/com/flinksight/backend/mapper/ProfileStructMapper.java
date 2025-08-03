package com.flinksight.backend.mapper;

import com.flinksight.backend.domain.Profile;
import com.flinksight.common.dto.ProfileDTO;
import org.mapstruct.Mapper;

@Mapper(config = BaseStructMapperConfig.class, componentModel = "spring")
public interface ProfileStructMapper extends GenericMapper<ProfileDTO, Profile> {}
