package com.flinksight.backend.mapper;

import org.mapstruct.Mapper;
import com.flinksight.backend.domain.JobInfo;
import com.flinksight.common.dto.JobInfoDTO;

@Mapper(config = BaseStructMapperConfig.class)
public interface JobInfoStructMapper extends GenericMapper<JobInfoDTO, JobInfo> {}
