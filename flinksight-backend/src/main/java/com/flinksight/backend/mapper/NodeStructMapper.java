package com.flinksight.backend.mapper;

import com.flinksight.backend.domain.Node;
import com.flinksight.common.dto.NodeDTO;
import org.mapstruct.Mapper;

@Mapper(config = BaseStructMapperConfig.class, componentModel = "spring")
public interface NodeStructMapper extends GenericMapper<NodeDTO, Node> {}
