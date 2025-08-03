package com.flinksight.backend.mapper;

import com.flinksight.backend.domain.JobAlertLog;
import com.flinksight.common.dto.JobAlertLogDTO;
import org.mapstruct.Mapper;

@Mapper(config = BaseStructMapperConfig.class, componentModel = "spring")
public interface JobAlertLogStructMapper extends GenericMapper<JobAlertLogDTO, JobAlertLog> {}
