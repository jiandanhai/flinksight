package com.flinksight.backend.mapper;

import com.flinksight.backend.domain.ApiAccessLog;
import com.flinksight.common.dto.ApiAccessLogDTO;
import org.mapstruct.Mapper;

@Mapper(config = BaseStructMapperConfig.class, componentModel = "spring")
public interface ApiAccessLogStructMapper extends GenericMapper<ApiAccessLogDTO, ApiAccessLog> {}
