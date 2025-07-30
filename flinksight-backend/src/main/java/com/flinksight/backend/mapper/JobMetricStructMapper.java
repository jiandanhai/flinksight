package com.flinksight.backend.mapper;

import org.mapstruct.Mapper;
import com.flinksight.backend.domain.JobMetric;
import com.flinksight.common.dto.JobMetricDTO;

@Mapper(config = BaseStructMapperConfig.class)
public interface JobMetricStructMapper extends GenericMapper<JobMetricDTO, JobMetric> {}
