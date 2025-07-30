package com.flinksight.backend.mapper;

import com.flinksight.backend.domain.UserPermission;
import com.flinksight.common.dto.UserPermissionDTO;
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
public class UserPermissionStructMapperImpl implements UserPermissionStructMapper {

    @Override
    public UserPermissionDTO toDTO(UserPermission entity) {
        if ( entity == null ) {
            return null;
        }

        UserPermissionDTO.UserPermissionDTOBuilder userPermissionDTO = UserPermissionDTO.builder();

        userPermissionDTO.id( entity.getId() );
        userPermissionDTO.userId( entity.getUserId() );
        userPermissionDTO.permissionId( entity.getPermissionId() );
        userPermissionDTO.isDeleted( entity.getIsDeleted() );

        return userPermissionDTO.build();
    }

    @Override
    public UserPermission toEntity(UserPermissionDTO dto) {
        if ( dto == null ) {
            return null;
        }

        UserPermission.UserPermissionBuilder userPermission = UserPermission.builder();

        userPermission.id( dto.getId() );
        userPermission.userId( dto.getUserId() );
        userPermission.permissionId( dto.getPermissionId() );
        userPermission.isDeleted( dto.getIsDeleted() );

        return userPermission.build();
    }

    @Override
    public List<UserPermissionDTO> toDTOList(List<UserPermission> entityList) {
        if ( entityList == null ) {
            return null;
        }

        List<UserPermissionDTO> list = new ArrayList<UserPermissionDTO>( entityList.size() );
        for ( UserPermission userPermission : entityList ) {
            list.add( toDTO( userPermission ) );
        }

        return list;
    }

    @Override
    public List<UserPermission> toEntityList(List<UserPermissionDTO> dtoList) {
        if ( dtoList == null ) {
            return null;
        }

        List<UserPermission> list = new ArrayList<UserPermission>( dtoList.size() );
        for ( UserPermissionDTO userPermissionDTO : dtoList ) {
            list.add( toEntity( userPermissionDTO ) );
        }

        return list;
    }
}
