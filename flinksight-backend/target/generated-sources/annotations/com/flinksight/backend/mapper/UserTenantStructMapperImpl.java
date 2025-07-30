package com.flinksight.backend.mapper;

import com.flinksight.backend.domain.UserTenant;
import com.flinksight.common.dto.UserTenantDTO;
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
public class UserTenantStructMapperImpl implements UserTenantStructMapper {

    @Override
    public UserTenantDTO toDTO(UserTenant entity) {
        if ( entity == null ) {
            return null;
        }

        UserTenantDTO.UserTenantDTOBuilder userTenantDTO = UserTenantDTO.builder();

        userTenantDTO.id( entity.getId() );
        userTenantDTO.userId( entity.getUserId() );
        userTenantDTO.tenantId( entity.getTenantId() );
        userTenantDTO.isDeleted( entity.getIsDeleted() );

        return userTenantDTO.build();
    }

    @Override
    public UserTenant toEntity(UserTenantDTO dto) {
        if ( dto == null ) {
            return null;
        }

        UserTenant.UserTenantBuilder userTenant = UserTenant.builder();

        userTenant.id( dto.getId() );
        userTenant.userId( dto.getUserId() );
        userTenant.tenantId( dto.getTenantId() );
        userTenant.isDeleted( dto.getIsDeleted() );

        return userTenant.build();
    }

    @Override
    public List<UserTenantDTO> toDTOList(List<UserTenant> entityList) {
        if ( entityList == null ) {
            return null;
        }

        List<UserTenantDTO> list = new ArrayList<UserTenantDTO>( entityList.size() );
        for ( UserTenant userTenant : entityList ) {
            list.add( toDTO( userTenant ) );
        }

        return list;
    }

    @Override
    public List<UserTenant> toEntityList(List<UserTenantDTO> dtoList) {
        if ( dtoList == null ) {
            return null;
        }

        List<UserTenant> list = new ArrayList<UserTenant>( dtoList.size() );
        for ( UserTenantDTO userTenantDTO : dtoList ) {
            list.add( toEntity( userTenantDTO ) );
        }

        return list;
    }
}
