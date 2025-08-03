package com.flinksight.backend.mapper;

import com.flinksight.backend.domain.AlertHistory;
import com.flinksight.common.dto.AlertHistoryDTO;
import org.mapstruct.Mapper;

@Mapper(config = BaseStructMapperConfig.class, componentModel = "spring")
public interface AlertHistoryStructMapper extends GenericMapper<AlertHistoryDTO, AlertHistory> {}
