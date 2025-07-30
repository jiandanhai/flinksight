package com.flinksight.backend.mapper;

import org.mapstruct.Mapper;
import com.flinksight.backend.domain.Notification;
import com.flinksight.common.dto.NotificationDTO;

@Mapper(config = BaseStructMapperConfig.class)
public interface NotificationStructMapper extends GenericMapper<NotificationDTO, Notification> {}
