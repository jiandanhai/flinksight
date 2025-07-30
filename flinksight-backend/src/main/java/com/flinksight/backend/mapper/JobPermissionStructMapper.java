package com.flinksight.backend.mapper;

import org.mapstruct.Mapper;
import com.flinksight.backend.domain.JobPermission;
import com.flinksight.common.dto.JobPermissionDTO;

@Mapper(config = BaseStructMapperConfig.class)
public interface JobPermissionStructMapper extends GenericMapper<JobPermissionDTO, JobPermission> {}
