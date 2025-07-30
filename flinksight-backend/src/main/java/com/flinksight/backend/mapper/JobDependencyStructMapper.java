package com.flinksight.backend.mapper;

import org.mapstruct.Mapper;
import com.flinksight.backend.domain.JobDependency;
import com.flinksight.common.dto.JobDependencyDTO;

@Mapper(config = BaseStructMapperConfig.class)
public interface JobDependencyStructMapper extends GenericMapper<JobDependencyDTO, JobDependency> {}
