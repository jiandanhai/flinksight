package com.flinksight.backend.mapper;

import com.flinksight.backend.domain.ResourceGroup;
import com.flinksight.common.dto.ResourceGroupDTO;
import org.mapstruct.Mapper;

@Mapper(config = BaseStructMapperConfig.class, componentModel = "spring")
public interface ResourceGroupStructMapper extends GenericMapper<ResourceGroupDTO, ResourceGroup> {}
