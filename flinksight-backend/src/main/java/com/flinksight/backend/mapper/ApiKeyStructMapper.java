package com.flinksight.backend.mapper;

import org.mapstruct.Mapper;
import com.flinksight.backend.domain.ApiKey;
import com.flinksight.common.dto.ApiKeyDTO;

@Mapper(config = BaseStructMapperConfig.class)
public interface ApiKeyStructMapper extends GenericMapper<ApiKeyDTO, ApiKey> {}
