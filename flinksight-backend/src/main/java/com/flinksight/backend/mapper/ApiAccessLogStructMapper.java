package com.flinksight.backend.mapper;

import org.mapstruct.Mapper;
import com.flinksight.backend.domain.ApiAccessLog;
import com.flinksight.common.dto.ApiAccessLogDTO;

@Mapper(config = BaseStructMapperConfig.class)
public interface ApiAccessLogStructMapper extends GenericMapper<ApiAccessLogDTO, ApiAccessLog> {}
