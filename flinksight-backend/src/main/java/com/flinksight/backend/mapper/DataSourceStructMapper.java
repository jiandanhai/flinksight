package com.flinksight.backend.mapper;

import org.mapstruct.Mapper;
import com.flinksight.backend.domain.DataSource;
import com.flinksight.common.dto.DataSourceDTO;

@Mapper(config = BaseStructMapperConfig.class)
public interface DataSourceStructMapper extends GenericMapper<DataSourceDTO, DataSource> {}
