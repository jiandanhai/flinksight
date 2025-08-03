package com.flinksight.backend.mapper;

import com.flinksight.backend.domain.JobMetric;
import com.flinksight.common.dto.JobMetricDTO;
import org.mapstruct.Mapper;

@Mapper(config = BaseStructMapperConfig.class, componentModel = "spring")
public interface JobMetricStructMapper extends GenericMapper<JobMetricDTO, JobMetric> {}
