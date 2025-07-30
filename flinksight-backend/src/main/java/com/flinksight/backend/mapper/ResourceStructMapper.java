package com.flinksight.backend.mapper;

import org.mapstruct.Mapper;
import com.flinksight.backend.domain.Resource;
import com.flinksight.common.dto.ResourceDTO;

@Mapper(config = BaseStructMapperConfig.class)
public interface ResourceStructMapper extends GenericMapper<ResourceDTO, Resource> {}
