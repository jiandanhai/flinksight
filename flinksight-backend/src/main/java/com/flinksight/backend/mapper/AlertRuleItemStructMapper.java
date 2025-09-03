package com.flinksight.backend.mapper;

import com.flinksight.backend.domain.AlertRuleItem;
import com.flinksight.common.dto.AlertRuleItemDTO;
import org.mapstruct.Mapper;

@Mapper(config = BaseStructMapperConfig.class, componentModel = "spring")
public interface AlertRuleItemStructMapper extends GenericMapper<AlertRuleItemDTO, AlertRuleItem> {}

