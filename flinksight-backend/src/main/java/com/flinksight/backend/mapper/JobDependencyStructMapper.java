package com.flinksight.backend.mapper;

import com.flinksight.backend.domain.JobDependency;
import com.flinksight.common.dto.JobDependencyDTO;
import org.mapstruct.Mapper;

@Mapper(config = BaseStructMapperConfig.class, componentModel = "spring")
public interface JobDependencyStructMapper extends GenericMapper<JobDependencyDTO, JobDependency> {}
