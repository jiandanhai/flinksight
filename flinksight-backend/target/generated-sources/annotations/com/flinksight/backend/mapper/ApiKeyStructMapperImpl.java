package com.flinksight.backend.mapper;

import com.flinksight.backend.domain.ApiKey;
import com.flinksight.common.dto.ApiKeyDTO;
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
public class ApiKeyStructMapperImpl implements ApiKeyStructMapper {

    @Override
    public ApiKeyDTO toDTO(ApiKey entity) {
        if ( entity == null ) {
            return null;
        }

        ApiKeyDTO apiKeyDTO = new ApiKeyDTO();

        apiKeyDTO.setId( entity.getId() );
        apiKeyDTO.setName( entity.getName() );
        apiKeyDTO.setApiKey( entity.getApiKey() );
        apiKeyDTO.setTenantId( entity.getTenantId() );
        apiKeyDTO.setUserId( entity.getUserId() );
        apiKeyDTO.setStatus( entity.getStatus() );
        apiKeyDTO.setExpireTime( entity.getExpireTime() );
        apiKeyDTO.setCreateTime( entity.getCreateTime() );
        apiKeyDTO.setIsDeleted( entity.getIsDeleted() );

        return apiKeyDTO;
    }

    @Override
    public ApiKey toEntity(ApiKeyDTO dto) {
        if ( dto == null ) {
            return null;
        }

        ApiKey.ApiKeyBuilder apiKey = ApiKey.builder();

        apiKey.id( dto.getId() );
        apiKey.name( dto.getName() );
        apiKey.apiKey( dto.getApiKey() );
        apiKey.tenantId( dto.getTenantId() );
        apiKey.userId( dto.getUserId() );
        apiKey.status( dto.getStatus() );
        apiKey.expireTime( dto.getExpireTime() );
        apiKey.createTime( dto.getCreateTime() );
        apiKey.isDeleted( dto.getIsDeleted() );

        return apiKey.build();
    }

    @Override
    public List<ApiKeyDTO> toDTOList(List<ApiKey> entityList) {
        if ( entityList == null ) {
            return null;
        }

        List<ApiKeyDTO> list = new ArrayList<ApiKeyDTO>( entityList.size() );
        for ( ApiKey apiKey : entityList ) {
            list.add( toDTO( apiKey ) );
        }

        return list;
    }

    @Override
    public List<ApiKey> toEntityList(List<ApiKeyDTO> dtoList) {
        if ( dtoList == null ) {
            return null;
        }

        List<ApiKey> list = new ArrayList<ApiKey>( dtoList.size() );
        for ( ApiKeyDTO apiKeyDTO : dtoList ) {
            list.add( toEntity( apiKeyDTO ) );
        }

        return list;
    }
}
