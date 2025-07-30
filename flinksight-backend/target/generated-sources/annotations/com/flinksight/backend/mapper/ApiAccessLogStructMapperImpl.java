package com.flinksight.backend.mapper;

import com.flinksight.backend.domain.ApiAccessLog;
import com.flinksight.common.dto.ApiAccessLogDTO;
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
public class ApiAccessLogStructMapperImpl implements ApiAccessLogStructMapper {

    @Override
    public ApiAccessLogDTO toDTO(ApiAccessLog entity) {
        if ( entity == null ) {
            return null;
        }

        ApiAccessLogDTO apiAccessLogDTO = new ApiAccessLogDTO();

        apiAccessLogDTO.setId( entity.getId() );
        apiAccessLogDTO.setUrl( entity.getUrl() );
        apiAccessLogDTO.setHttpMethod( entity.getHttpMethod() );
        apiAccessLogDTO.setParams( entity.getParams() );
        apiAccessLogDTO.setStatus( entity.getStatus() );
        apiAccessLogDTO.setUserId( entity.getUserId() );
        apiAccessLogDTO.setTenantId( entity.getTenantId() );
        apiAccessLogDTO.setIp( entity.getIp() );
        apiAccessLogDTO.setAccessTime( entity.getAccessTime() );
        apiAccessLogDTO.setDuration( entity.getDuration() );
        apiAccessLogDTO.setIsDeleted( entity.getIsDeleted() );

        return apiAccessLogDTO;
    }

    @Override
    public ApiAccessLog toEntity(ApiAccessLogDTO dto) {
        if ( dto == null ) {
            return null;
        }

        ApiAccessLog.ApiAccessLogBuilder apiAccessLog = ApiAccessLog.builder();

        apiAccessLog.id( dto.getId() );
        apiAccessLog.url( dto.getUrl() );
        apiAccessLog.httpMethod( dto.getHttpMethod() );
        apiAccessLog.params( dto.getParams() );
        apiAccessLog.status( dto.getStatus() );
        apiAccessLog.userId( dto.getUserId() );
        apiAccessLog.tenantId( dto.getTenantId() );
        apiAccessLog.ip( dto.getIp() );
        apiAccessLog.accessTime( dto.getAccessTime() );
        apiAccessLog.duration( dto.getDuration() );
        apiAccessLog.isDeleted( dto.getIsDeleted() );

        return apiAccessLog.build();
    }

    @Override
    public List<ApiAccessLogDTO> toDTOList(List<ApiAccessLog> entityList) {
        if ( entityList == null ) {
            return null;
        }

        List<ApiAccessLogDTO> list = new ArrayList<ApiAccessLogDTO>( entityList.size() );
        for ( ApiAccessLog apiAccessLog : entityList ) {
            list.add( toDTO( apiAccessLog ) );
        }

        return list;
    }

    @Override
    public List<ApiAccessLog> toEntityList(List<ApiAccessLogDTO> dtoList) {
        if ( dtoList == null ) {
            return null;
        }

        List<ApiAccessLog> list = new ArrayList<ApiAccessLog>( dtoList.size() );
        for ( ApiAccessLogDTO apiAccessLogDTO : dtoList ) {
            list.add( toEntity( apiAccessLogDTO ) );
        }

        return list;
    }
}
