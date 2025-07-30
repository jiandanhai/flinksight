package com.flinksight.backend.mapper;

import org.mapstruct.Mapper;
import com.flinksight.backend.domain.JobInstance;
import com.flinksight.common.dto.JobInstanceDTO;

@Mapper(config = BaseStructMapperConfig.class)
public interface JobInstanceStructMapper extends GenericMapper<JobInstanceDTO, JobInstance> {}
