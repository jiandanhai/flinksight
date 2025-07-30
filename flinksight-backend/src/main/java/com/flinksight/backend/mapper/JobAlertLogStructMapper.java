package com.flinksight.backend.mapper;

import org.mapstruct.Mapper;
import com.flinksight.backend.domain.JobAlertLog;
import com.flinksight.common.dto.JobAlertLogDTO;

@Mapper(config = BaseStructMapperConfig.class)
public interface JobAlertLogStructMapper extends GenericMapper<JobAlertLogDTO, JobAlertLog> {}
