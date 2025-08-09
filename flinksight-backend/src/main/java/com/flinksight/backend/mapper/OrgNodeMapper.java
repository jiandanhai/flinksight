package com.flinksight.backend.mapper;

import com.flinksight.backend.domain.OrgNode;
import com.flinksight.common.dto.OrgNodeDTO;
import org.mapstruct.Mapper;

@Mapper(config = BaseStructMapperConfig.class, componentModel = "spring")
public interface OrgNodeMapper  extends GenericMapper<OrgNodeDTO, OrgNode> {}
