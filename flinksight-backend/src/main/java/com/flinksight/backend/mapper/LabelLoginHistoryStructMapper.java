package com.flinksight.backend.mapper;

import com.flinksight.backend.domain.LabelLoginHistory;
import com.flinksight.common.dto.LabelLoginHistoryDTO;
import org.mapstruct.Mapper;

@Mapper(config = BaseStructMapperConfig.class, componentModel = "spring")
public interface LabelLoginHistoryStructMapper extends GenericMapper<LabelLoginHistoryDTO, LabelLoginHistory> {}
