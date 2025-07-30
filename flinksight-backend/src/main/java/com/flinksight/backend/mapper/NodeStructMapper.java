package com.flinksight.backend.mapper;

import org.mapstruct.Mapper;
import com.flinksight.backend.domain.Node;
import com.flinksight.common.dto.NodeDTO;

@Mapper(config = BaseStructMapperConfig.class)
public interface NodeStructMapper extends GenericMapper<NodeDTO, Node> {}
