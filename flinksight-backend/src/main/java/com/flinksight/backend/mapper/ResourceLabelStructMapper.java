package com.flinksight.backend.mapper;

import com.flinksight.backend.domain.ResourceLabel;
import com.flinksight.common.dto.ResourceLabelDTO;
import org.mapstruct.Mapper;

@Mapper(config = BaseStructMapperConfig.class, componentModel = "spring")
public interface ResourceLabelStructMapper extends GenericMapper<ResourceLabelDTO, ResourceLabel> {}
