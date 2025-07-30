package com.flinksight.backend.mapper;

import com.flinksight.backend.domain.Permission;
import com.flinksight.common.dto.PermissionDTO;
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
public class PermissionStructMapperImpl implements PermissionStructMapper {

    @Override
    public PermissionDTO toDTO(Permission entity) {
        if ( entity == null ) {
            return null;
        }

        PermissionDTO.PermissionDTOBuilder permissionDTO = PermissionDTO.builder();

        permissionDTO.id( entity.getId() );
        permissionDTO.code( entity.getCode() );
        permissionDTO.name( entity.getName() );
        permissionDTO.desc( entity.getDesc() );
        permissionDTO.type( entity.getType() );
        permissionDTO.isDeleted( entity.getIsDeleted() );

        return permissionDTO.build();
    }

    @Override
    public Permission toEntity(PermissionDTO dto) {
        if ( dto == null ) {
            return null;
        }

        Permission.PermissionBuilder permission = Permission.builder();

        permission.id( dto.getId() );
        permission.code( dto.getCode() );
        permission.name( dto.getName() );
        permission.desc( dto.getDesc() );
        permission.type( dto.getType() );
        permission.isDeleted( dto.getIsDeleted() );

        return permission.build();
    }

    @Override
    public List<PermissionDTO> toDTOList(List<Permission> entityList) {
        if ( entityList == null ) {
            return null;
        }

        List<PermissionDTO> list = new ArrayList<PermissionDTO>( entityList.size() );
        for ( Permission permission : entityList ) {
            list.add( toDTO( permission ) );
        }

        return list;
    }

    @Override
    public List<Permission> toEntityList(List<PermissionDTO> dtoList) {
        if ( dtoList == null ) {
            return null;
        }

        List<Permission> list = new ArrayList<Permission>( dtoList.size() );
        for ( PermissionDTO permissionDTO : dtoList ) {
            list.add( toEntity( permissionDTO ) );
        }

        return list;
    }
}
