package com.flinksight.backend.mapper;

import com.flinksight.backend.domain.AlertRule;
import com.flinksight.common.dto.AlertRuleDTO;
import org.mapstruct.Mapper;

@Mapper(config = BaseStructMapperConfig.class, componentModel = "spring")
public interface AlertRuleStructMapper extends GenericMapper<AlertRuleDTO, AlertRule> {}
