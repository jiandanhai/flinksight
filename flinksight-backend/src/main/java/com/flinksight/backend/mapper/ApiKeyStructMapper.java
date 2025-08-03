package com.flinksight.backend.mapper;

import com.flinksight.backend.domain.ApiKey;
import com.flinksight.common.dto.ApiKeyDTO;
import org.mapstruct.Mapper;

@Mapper(config = BaseStructMapperConfig.class, componentModel = "spring")
public interface ApiKeyStructMapper extends GenericMapper<ApiKeyDTO, ApiKey> {}
