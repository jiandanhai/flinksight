package com.flinksight.backend.mapper;

import org.mapstruct.Mapper;
import com.flinksight.backend.domain.ResourceGroup;
import com.flinksight.common.dto.ResourceGroupDTO;

@Mapper(config = BaseStructMapperConfig.class)
public interface ResourceGroupStructMapper extends GenericMapper<ResourceGroupDTO, ResourceGroup> {}
