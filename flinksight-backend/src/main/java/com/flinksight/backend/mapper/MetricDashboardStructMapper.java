package com.flinksight.backend.mapper;

import com.flinksight.backend.domain.MetricDashboard;
import com.flinksight.common.dto.MetricDashboardDTO;
import org.mapstruct.Mapper;

@Mapper(config = BaseStructMapperConfig.class, componentModel = "spring")
public interface MetricDashboardStructMapper extends GenericMapper<MetricDashboardDTO, MetricDashboard> {}
