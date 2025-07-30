package com.flinksight.backend.mapper;

import org.mapstruct.Mapper;
import com.flinksight.backend.domain.JobAlertRule;
import com.flinksight.common.dto.JobAlertRuleDTO;

@Mapper(config = BaseStructMapperConfig.class)
public interface JobAlertRuleStructMapper extends GenericMapper<JobAlertRuleDTO, JobAlertRule> {}
