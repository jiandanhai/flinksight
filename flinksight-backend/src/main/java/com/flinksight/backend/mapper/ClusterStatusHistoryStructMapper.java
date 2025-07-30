package com.flinksight.backend.mapper;

import org.mapstruct.Mapper;
import com.flinksight.backend.domain.ClusterStatusHistory;
import com.flinksight.common.dto.ClusterStatusHistoryDTO;

@Mapper(config = BaseStructMapperConfig.class)
public interface ClusterStatusHistoryStructMapper extends GenericMapper<ClusterStatusHistoryDTO, ClusterStatusHistory> {}
