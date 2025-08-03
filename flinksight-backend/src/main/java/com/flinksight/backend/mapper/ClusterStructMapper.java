package com.flinksight.backend.mapper;

import com.flinksight.backend.domain.Cluster;
import com.flinksight.common.dto.ClusterDTO;
import org.mapstruct.Mapper;

@Mapper(config = BaseStructMapperConfig.class, componentModel = "spring")
public interface ClusterStructMapper extends GenericMapper<ClusterDTO, Cluster> {}
