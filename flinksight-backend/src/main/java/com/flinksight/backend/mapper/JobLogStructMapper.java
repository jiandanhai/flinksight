package com.flinksight.backend.mapper;

import org.mapstruct.Mapper;
import com.flinksight.backend.domain.JobLog;
import com.flinksight.common.dto.JobLogDTO;

@Mapper(config = BaseStructMapperConfig.class)
public interface JobLogStructMapper extends GenericMapper<JobLogDTO, JobLog> {}
