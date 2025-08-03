package com.flinksight.backend.mapper;

import com.flinksight.backend.domain.JobLog;
import com.flinksight.common.dto.JobLogDTO;
import org.mapstruct.Mapper;

@Mapper(config = BaseStructMapperConfig.class, componentModel = "spring")
public interface JobLogStructMapper extends GenericMapper<JobLogDTO, JobLog> {}
