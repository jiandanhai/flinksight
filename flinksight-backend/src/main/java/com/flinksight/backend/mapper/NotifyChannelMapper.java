package com.flinksight.backend.mapper;

import com.flinksight.backend.domain.NotifyChannel;
import com.flinksight.common.dto.NotifyChannelDTO;
import org.mapstruct.Mapper;

@Mapper(config = BaseStructMapperConfig.class, componentModel = "spring")
public interface NotifyChannelMapper  extends GenericMapper<NotifyChannelDTO, NotifyChannel> {
}
