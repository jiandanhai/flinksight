package com.flinksight.backend.mapper;

import org.mapstruct.Mapper;
import com.flinksight.backend.domain.Profile;
import com.flinksight.common.dto.ProfileDTO;

@Mapper(config = BaseStructMapperConfig.class)
public interface ProfileStructMapper extends GenericMapper<ProfileDTO, Profile> {}
