package com.flinksight.backend.mapper;

import com.flinksight.backend.domain.Resource;
import com.flinksight.common.dto.ResourceDTO;
import org.mapstruct.Mapper;

@Mapper(config = BaseStructMapperConfig.class, componentModel = "spring")
public interface ResourceStructMapper extends GenericMapper<ResourceDTO, Resource> {}
