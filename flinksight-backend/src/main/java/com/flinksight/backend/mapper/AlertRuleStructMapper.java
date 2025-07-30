package com.flinksight.backend.mapper;

import org.mapstruct.Mapper;
import com.flinksight.backend.domain.AlertRule;
import com.flinksight.common.dto.AlertRuleDTO;

@Mapper(config = BaseStructMapperConfig.class)
public interface AlertRuleStructMapper extends GenericMapper<AlertRuleDTO, AlertRule> {}
