package com.flinksight.backend.mapper;

import com.flinksight.backend.domain.Notification;
import com.flinksight.common.dto.NotificationDTO;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2025-07-30T19:25:52+0800",
    comments = "version: 1.5.5.Final, compiler: javac, environment: Java 17.0.13 (Oracle Corporation)"
)
@Component
public class NotificationStructMapperImpl implements NotificationStructMapper {

    @Override
    public NotificationDTO toDTO(Notification entity) {
        if ( entity == null ) {
            return null;
        }

        NotificationDTO.NotificationDTOBuilder notificationDTO = NotificationDTO.builder();

        notificationDTO.id( entity.getId() );
        notificationDTO.title( entity.getTitle() );
        notificationDTO.content( entity.getContent() );
        if ( entity.getType() != null ) {
            notificationDTO.type( Integer.parseInt( entity.getType() ) );
        }
        notificationDTO.userId( entity.getUserId() );
        notificationDTO.tenantId( entity.getTenantId() );
        notificationDTO.isDeleted( entity.getIsDeleted() );

        return notificationDTO.build();
    }

    @Override
    public Notification toEntity(NotificationDTO dto) {
        if ( dto == null ) {
            return null;
        }

        Notification.NotificationBuilder notification = Notification.builder();

        notification.id( dto.getId() );
        notification.userId( dto.getUserId() );
        notification.tenantId( dto.getTenantId() );
        notification.title( dto.getTitle() );
        notification.content( dto.getContent() );
        if ( dto.getType() != null ) {
            notification.type( String.valueOf( dto.getType() ) );
        }
        notification.isDeleted( dto.getIsDeleted() );

        return notification.build();
    }

    @Override
    public List<NotificationDTO> toDTOList(List<Notification> entityList) {
        if ( entityList == null ) {
            return null;
        }

        List<NotificationDTO> list = new ArrayList<NotificationDTO>( entityList.size() );
        for ( Notification notification : entityList ) {
            list.add( toDTO( notification ) );
        }

        return list;
    }

    @Override
    public List<Notification> toEntityList(List<NotificationDTO> dtoList) {
        if ( dtoList == null ) {
            return null;
        }

        List<Notification> list = new ArrayList<Notification>( dtoList.size() );
        for ( NotificationDTO notificationDTO : dtoList ) {
            list.add( toEntity( notificationDTO ) );
        }

        return list;
    }
}
