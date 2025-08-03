package com.flinksight.backend.mapper;

import com.flinksight.backend.domain.JobInfo;
import com.flinksight.common.dto.JobInfoDTO;
import org.mapstruct.Mapper;

@Mapper(config = BaseStructMapperConfig.class, componentModel = "spring")
public interface JobInfoStructMapper extends GenericMapper<JobInfoDTO, JobInfo> {}
