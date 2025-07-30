package com.flinksight.backend.mapper;

import org.mapstruct.Mapper;
import com.flinksight.backend.domain.LabelLoginHistory;
import com.flinksight.common.dto.LabelLoginHistoryDTO;

@Mapper(config = BaseStructMapperConfig.class)
public interface LabelLoginHistoryStructMapper extends GenericMapper<LabelLoginHistoryDTO, LabelLoginHistory> {}
