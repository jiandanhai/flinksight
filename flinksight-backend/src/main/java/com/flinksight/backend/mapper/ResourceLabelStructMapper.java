package com.flinksight.backend.mapper;

import org.mapstruct.Mapper;
import com.flinksight.backend.domain.ResourceLabel;
import com.flinksight.common.dto.ResourceLabelDTO;

@Mapper(config = BaseStructMapperConfig.class)
public interface ResourceLabelStructMapper extends GenericMapper<ResourceLabelDTO, ResourceLabel> {}
