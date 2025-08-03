package com.flinksight.backend.mapper;

import com.flinksight.backend.domain.JobDiagnosticLog;
import com.flinksight.common.dto.JobDiagnosticLogDTO;
import org.mapstruct.Mapper;

@Mapper(config = BaseStructMapperConfig.class, componentModel = "spring")
public interface JobDiagnosticLogStructMapper extends GenericMapper<JobDiagnosticLogDTO, JobDiagnosticLog> {}
