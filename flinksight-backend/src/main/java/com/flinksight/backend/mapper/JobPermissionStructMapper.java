package com.flinksight.backend.mapper;

import com.flinksight.backend.domain.JobPermission;
import com.flinksight.common.dto.JobPermissionDTO;
import org.mapstruct.Mapper;

@Mapper(config = BaseStructMapperConfig.class, componentModel = "spring")
public interface JobPermissionStructMapper extends GenericMapper<JobPermissionDTO, JobPermission> {}
