package com.flinksight.backend.mapper;

import com.flinksight.backend.domain.Alert;
import com.flinksight.common.dto.AlertDTO;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2025-07-30T19:25:54+0800",
    comments = "version: 1.5.5.Final, compiler: javac, environment: Java 17.0.13 (Oracle Corporation)"
)
@Component
public class AlertStructMapperImpl implements AlertStructMapper {

    @Override
    public AlertDTO toDTO(Alert entity) {
        if ( entity == null ) {
            return null;
        }

        AlertDTO.AlertDTOBuilder alertDTO = AlertDTO.builder();

        alertDTO.id( entity.getId() );
        alertDTO.tenantId( entity.getTenantId() );
        alertDTO.jobId( entity.getJobId() );
        alertDTO.level( entity.getLevel() );
        alertDTO.type( entity.getType() );
        alertDTO.message( entity.getMessage() );
        alertDTO.status( entity.getStatus() );
        alertDTO.handlerId( entity.getHandlerId() );
        alertDTO.isDeleted( entity.getIsDeleted() );
        alertDTO.createTime( entity.getCreateTime() );
        alertDTO.updateTime( entity.getUpdateTime() );

        return alertDTO.build();
    }

    @Override
    public Alert toEntity(AlertDTO dto) {
        if ( dto == null ) {
            return null;
        }

        Alert.AlertBuilder alert = Alert.builder();

        alert.id( dto.getId() );
        alert.tenantId( dto.getTenantId() );
        alert.jobId( dto.getJobId() );
        alert.level( dto.getLevel() );
        alert.type( dto.getType() );
        alert.message( dto.getMessage() );
        alert.status( dto.getStatus() );
        alert.handlerId( dto.getHandlerId() );
        alert.isDeleted( dto.getIsDeleted() );
        alert.createTime( dto.getCreateTime() );
        alert.updateTime( dto.getUpdateTime() );

        return alert.build();
    }

    @Override
    public List<AlertDTO> toDTOList(List<Alert> entityList) {
        if ( entityList == null ) {
            return null;
        }

        List<AlertDTO> list = new ArrayList<AlertDTO>( entityList.size() );
        for ( Alert alert : entityList ) {
            list.add( toDTO( alert ) );
        }

        return list;
    }

    @Override
    public List<Alert> toEntityList(List<AlertDTO> dtoList) {
        if ( dtoList == null ) {
            return null;
        }

        List<Alert> list = new ArrayList<Alert>( dtoList.size() );
        for ( AlertDTO alertDTO : dtoList ) {
            list.add( toEntity( alertDTO ) );
        }

        return list;
    }
}
