package com.flinksight.backend.mapper;

import com.flinksight.backend.domain.ApiWhitelist;
import com.flinksight.common.dto.ApiWhitelistDTO;
import org.mapstruct.Mapper;

@Mapper(config = BaseStructMapperConfig.class, componentModel = "spring")
public interface ApiWhitelistStructMapper extends GenericMapper<ApiWhitelistDTO, ApiWhitelist> {}
