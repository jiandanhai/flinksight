package com.flinksight.backend.mapper;

import com.flinksight.backend.domain.LoginHistory;
import com.flinksight.common.dto.LoginHistoryDTO;
import org.mapstruct.Mapper;

@Mapper(config = BaseStructMapperConfig.class, componentModel = "spring")
public interface LoginHistoryStructMapper extends GenericMapper<LoginHistoryDTO, LoginHistory> {}
