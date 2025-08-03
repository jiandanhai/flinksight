package com.flinksight.backend.mapper;

import com.flinksight.backend.domain.NodeHealth;
import com.flinksight.common.dto.NodeHealthDTO;
import org.mapstruct.Mapper;

@Mapper(config = BaseStructMapperConfig.class, componentModel = "spring")
public interface NodeHealthStructMapper extends GenericMapper<NodeHealthDTO, NodeHealth> {}
