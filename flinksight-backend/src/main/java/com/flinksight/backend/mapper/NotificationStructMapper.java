package com.flinksight.backend.mapper;

import com.flinksight.backend.domain.Notification;
import com.flinksight.common.dto.NotificationDTO;
import org.mapstruct.Mapper;

@Mapper(config = BaseStructMapperConfig.class, componentModel = "spring")
public interface NotificationStructMapper extends GenericMapper<NotificationDTO, Notification> {}
