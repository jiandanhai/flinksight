package com.flinksight.backend.mapper;

import com.flinksight.backend.domain.ClusterStatusHistory;
import com.flinksight.common.dto.ClusterStatusHistoryDTO;
import org.mapstruct.Mapper;

@Mapper(config = BaseStructMapperConfig.class, componentModel = "spring")
public interface ClusterStatusHistoryStructMapper extends GenericMapper<ClusterStatusHistoryDTO, ClusterStatusHistory> {}
