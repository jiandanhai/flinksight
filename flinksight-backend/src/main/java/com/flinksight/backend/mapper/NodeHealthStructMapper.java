package com.flinksight.backend.mapper;

import org.mapstruct.Mapper;
import com.flinksight.backend.domain.NodeHealth;
import com.flinksight.common.dto.NodeHealthDTO;

@Mapper(config = BaseStructMapperConfig.class)
public interface NodeHealthStructMapper extends GenericMapper<NodeHealthDTO, NodeHealth> {}
