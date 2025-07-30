package com.flinksight.backend.mapper;

import com.flinksight.backend.domain.AuditLog;
import com.flinksight.common.dto.AuditLogDTO;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2025-07-30T19:25:53+0800",
    comments = "version: 1.5.5.Final, compiler: javac, environment: Java 17.0.13 (Oracle Corporation)"
)
@Component
public class AuditLogStructMapperImpl implements AuditLogStructMapper {

    @Override
    public AuditLogDTO toDTO(AuditLog entity) {
        if ( entity == null ) {
            return null;
        }

        AuditLogDTO.AuditLogDTOBuilder auditLogDTO = AuditLogDTO.builder();

        auditLogDTO.id( entity.getId() );
        auditLogDTO.userId( entity.getUserId() );
        auditLogDTO.tenantId( entity.getTenantId() );
        auditLogDTO.action( entity.getAction() );
        auditLogDTO.targetType( entity.getTargetType() );
        if ( entity.getTargetId() != null ) {
            auditLogDTO.targetId( String.valueOf( entity.getTargetId() ) );
        }
        auditLogDTO.content( entity.getContent() );
        auditLogDTO.traceId( entity.getTraceId() );
        auditLogDTO.ip( entity.getIp() );
        auditLogDTO.createTime( entity.getCreateTime() );
        auditLogDTO.isDeleted( entity.getIsDeleted() );

        return auditLogDTO.build();
    }

    @Override
    public AuditLog toEntity(AuditLogDTO dto) {
        if ( dto == null ) {
            return null;
        }

        AuditLog.AuditLogBuilder auditLog = AuditLog.builder();

        auditLog.id( dto.getId() );
        auditLog.tenantId( dto.getTenantId() );
        auditLog.userId( dto.getUserId() );
        auditLog.action( dto.getAction() );
        auditLog.targetType( dto.getTargetType() );
        if ( dto.getTargetId() != null ) {
            auditLog.targetId( Long.parseLong( dto.getTargetId() ) );
        }
        auditLog.ip( dto.getIp() );
        auditLog.content( dto.getContent() );
        auditLog.traceId( dto.getTraceId() );
        auditLog.createTime( dto.getCreateTime() );
        auditLog.isDeleted( dto.getIsDeleted() );

        return auditLog.build();
    }

    @Override
    public List<AuditLogDTO> toDTOList(List<AuditLog> entityList) {
        if ( entityList == null ) {
            return null;
        }

        List<AuditLogDTO> list = new ArrayList<AuditLogDTO>( entityList.size() );
        for ( AuditLog auditLog : entityList ) {
            list.add( toDTO( auditLog ) );
        }

        return list;
    }

    @Override
    public List<AuditLog> toEntityList(List<AuditLogDTO> dtoList) {
        if ( dtoList == null ) {
            return null;
        }

        List<AuditLog> list = new ArrayList<AuditLog>( dtoList.size() );
        for ( AuditLogDTO auditLogDTO : dtoList ) {
            list.add( toEntity( auditLogDTO ) );
        }

        return list;
    }
}
