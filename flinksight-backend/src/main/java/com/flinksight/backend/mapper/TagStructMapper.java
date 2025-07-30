package com.flinksight.backend.mapper;

import org.mapstruct.Mapper;
import com.flinksight.backend.domain.Tag;
import com.flinksight.common.dto.TagDTO;

@Mapper(config = BaseStructMapperConfig.class)
public interface TagStructMapper extends GenericMapper<TagDTO, Tag> {}
