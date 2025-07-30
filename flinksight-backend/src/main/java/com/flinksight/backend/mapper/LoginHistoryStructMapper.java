package com.flinksight.backend.mapper;

import org.mapstruct.Mapper;
import com.flinksight.backend.domain.LoginHistory;
import com.flinksight.common.dto.LoginHistoryDTO;

@Mapper(config = BaseStructMapperConfig.class)
public interface LoginHistoryStructMapper extends GenericMapper<LoginHistoryDTO, LoginHistory> {}
