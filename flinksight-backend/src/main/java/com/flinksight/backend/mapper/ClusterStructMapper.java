package com.flinksight.backend.mapper;

import org.mapstruct.Mapper;
import com.flinksight.backend.domain.Cluster;
import com.flinksight.common.dto.ClusterDTO;

@Mapper(config = BaseStructMapperConfig.class)
public interface ClusterStructMapper extends GenericMapper<ClusterDTO, Cluster> {}
