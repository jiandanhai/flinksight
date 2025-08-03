package com.flinksight.backend.mapper;

import com.flinksight.backend.domain.JobAlertRule;
import com.flinksight.common.dto.JobAlertRuleDTO;
import org.mapstruct.Mapper;

@Mapper(config = BaseStructMapperConfig.class, componentModel = "spring")
public interface JobAlertRuleStructMapper extends GenericMapper<JobAlertRuleDTO, JobAlertRule> {}
