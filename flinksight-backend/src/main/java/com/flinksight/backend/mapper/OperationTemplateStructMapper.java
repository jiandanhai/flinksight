package com.flinksight.backend.mapper;

import com.flinksight.backend.domain.OperationTemplate;
import com.flinksight.common.dto.OperationTemplateDTO;
import org.mapstruct.Mapper;

@Mapper(config = BaseStructMapperConfig.class, componentModel = "spring")
public interface OperationTemplateStructMapper extends GenericMapper<OperationTemplateDTO, OperationTemplate> {}
