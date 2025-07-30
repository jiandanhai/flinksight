package com.flinksight.backend.mapper;

import org.mapstruct.Mapper;
import com.flinksight.backend.domain.SystemSettings;
import com.flinksight.common.dto.SystemSettingsDTO;

@Mapper(config = BaseStructMapperConfig.class)
public interface SystemSettingsStructMapper extends GenericMapper<SystemSettingsDTO, SystemSettings> {}
