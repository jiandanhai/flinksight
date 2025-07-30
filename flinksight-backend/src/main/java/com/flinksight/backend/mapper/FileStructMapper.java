package com.flinksight.backend.mapper;

import org.mapstruct.Mapper;
import com.flinksight.backend.domain.File;
import com.flinksight.common.dto.FileDTO;

@Mapper(config = BaseStructMapperConfig.class)
public interface FileStructMapper extends GenericMapper<FileDTO, File> {}
