package com.flinksight.backend.mapper;

import org.mapstruct.Mapper;
import com.flinksight.backend.domain.MetricDashboard;
import com.flinksight.common.dto.MetricDashboardDTO;

@Mapper(config = BaseStructMapperConfig.class)
public interface MetricDashboardStructMapper extends GenericMapper<MetricDashboardDTO, MetricDashboard> {}
