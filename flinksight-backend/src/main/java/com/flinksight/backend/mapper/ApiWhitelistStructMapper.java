package com.flinksight.backend.mapper;

import org.mapstruct.Mapper;
import com.flinksight.backend.domain.ApiWhitelist;
import com.flinksight.common.dto.ApiWhitelistDTO;

@Mapper(config = BaseStructMapperConfig.class)
public interface ApiWhitelistStructMapper extends GenericMapper<ApiWhitelistDTO, ApiWhitelist> {}
