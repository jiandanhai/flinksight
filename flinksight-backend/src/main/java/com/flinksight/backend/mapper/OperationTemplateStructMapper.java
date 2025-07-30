package com.flinksight.backend.mapper;

import org.mapstruct.Mapper;
import com.flinksight.backend.domain.OperationTemplate;
import com.flinksight.common.dto.OperationTemplateDTO;

@Mapper(config = BaseStructMapperConfig.class)
public interface OperationTemplateStructMapper extends GenericMapper<OperationTemplateDTO, OperationTemplate> {}
