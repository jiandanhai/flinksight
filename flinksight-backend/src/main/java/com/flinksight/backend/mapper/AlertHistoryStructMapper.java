package com.flinksight.backend.mapper;

import org.mapstruct.Mapper;
import com.flinksight.backend.domain.AlertHistory;
import com.flinksight.common.dto.AlertHistoryDTO;

@Mapper(config = BaseStructMapperConfig.class)
public interface AlertHistoryStructMapper extends GenericMapper<AlertHistoryDTO, AlertHistory> {}
