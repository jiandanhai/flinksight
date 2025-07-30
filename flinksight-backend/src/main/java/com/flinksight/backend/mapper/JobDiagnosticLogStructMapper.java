package com.flinksight.backend.mapper;

import org.mapstruct.Mapper;
import com.flinksight.backend.domain.JobDiagnosticLog;
import com.flinksight.common.dto.JobDiagnosticLogDTO;

@Mapper(config = BaseStructMapperConfig.class)
public interface JobDiagnosticLogStructMapper extends GenericMapper<JobDiagnosticLogDTO, JobDiagnosticLog> {}
