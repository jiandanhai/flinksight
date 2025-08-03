package com.flinksight.backend.mapper;

import com.flinksight.backend.domain.JobInstance;
import com.flinksight.common.dto.JobInstanceDTO;
import org.mapstruct.Mapper;

@Mapper(config = BaseStructMapperConfig.class, componentModel = "spring")
public interface JobInstanceStructMapper extends GenericMapper<JobInstanceDTO, JobInstance> {}
