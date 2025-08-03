package com.flinksight.backend.mapper;

import com.flinksight.backend.domain.SystemSettings;
import com.flinksight.common.dto.SystemSettingsDTO;
import org.mapstruct.Mapper;

@Mapper(config = BaseStructMapperConfig.class, componentModel = "spring")
public interface SystemSettingsStructMapper extends GenericMapper<SystemSettingsDTO, SystemSettings> {}
