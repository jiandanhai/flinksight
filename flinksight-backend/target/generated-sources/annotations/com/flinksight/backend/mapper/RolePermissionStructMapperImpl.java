package com.flinksight.backend.mapper;

import com.flinksight.backend.domain.RolePermission;
import com.flinksight.common.dto.RolePermissionDTO;
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
public class RolePermissionStructMapperImpl implements RolePermissionStructMapper {

    @Override
    public RolePermissionDTO toDTO(RolePermission entity) {
        if ( entity == null ) {
            return null;
        }

        RolePermissionDTO.RolePermissionDTOBuilder rolePermissionDTO = RolePermissionDTO.builder();

        rolePermissionDTO.id( entity.getId() );
        rolePermissionDTO.roleId( entity.getRoleId() );
        rolePermissionDTO.permissionId( entity.getPermissionId() );
        rolePermissionDTO.isDeleted( entity.getIsDeleted() );

        return rolePermissionDTO.build();
    }

    @Override
    public RolePermission toEntity(RolePermissionDTO dto) {
        if ( dto == null ) {
            return null;
        }

        RolePermission.RolePermissionBuilder rolePermission = RolePermission.builder();

        rolePermission.id( dto.getId() );
        rolePermission.roleId( dto.getRoleId() );
        rolePermission.permissionId( dto.getPermissionId() );
        rolePermission.isDeleted( dto.getIsDeleted() );

        return rolePermission.build();
    }

    @Override
    public List<RolePermissionDTO> toDTOList(List<RolePermission> entityList) {
        if ( entityList == null ) {
            return null;
        }

        List<RolePermissionDTO> list = new ArrayList<RolePermissionDTO>( entityList.size() );
        for ( RolePermission rolePermission : entityList ) {
            list.add( toDTO( rolePermission ) );
        }

        return list;
    }

    @Override
    public List<RolePermission> toEntityList(List<RolePermissionDTO> dtoList) {
        if ( dtoList == null ) {
            return null;
        }

        List<RolePermission> list = new ArrayList<RolePermission>( dtoList.size() );
        for ( RolePermissionDTO rolePermissionDTO : dtoList ) {
            list.add( toEntity( rolePermissionDTO ) );
        }

        return list;
    }
}
