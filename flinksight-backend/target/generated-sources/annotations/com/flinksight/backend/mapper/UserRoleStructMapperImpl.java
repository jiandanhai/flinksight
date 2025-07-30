package com.flinksight.backend.mapper;

import com.flinksight.backend.domain.UserRole;
import com.flinksight.common.dto.UserRoleDTO;
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
public class UserRoleStructMapperImpl implements UserRoleStructMapper {

    @Override
    public UserRoleDTO toDTO(UserRole entity) {
        if ( entity == null ) {
            return null;
        }

        UserRoleDTO.UserRoleDTOBuilder userRoleDTO = UserRoleDTO.builder();

        userRoleDTO.id( entity.getId() );
        userRoleDTO.userId( entity.getUserId() );
        userRoleDTO.roleId( entity.getRoleId() );
        userRoleDTO.tenantId( entity.getTenantId() );
        userRoleDTO.assignTime( entity.getAssignTime() );
        userRoleDTO.isDeleted( entity.getIsDeleted() );

        return userRoleDTO.build();
    }

    @Override
    public UserRole toEntity(UserRoleDTO dto) {
        if ( dto == null ) {
            return null;
        }

        UserRole.UserRoleBuilder userRole = UserRole.builder();

        userRole.id( dto.getId() );
        userRole.userId( dto.getUserId() );
        userRole.roleId( dto.getRoleId() );
        userRole.tenantId( dto.getTenantId() );
        userRole.assignTime( dto.getAssignTime() );
        userRole.isDeleted( dto.getIsDeleted() );

        return userRole.build();
    }

    @Override
    public List<UserRoleDTO> toDTOList(List<UserRole> entityList) {
        if ( entityList == null ) {
            return null;
        }

        List<UserRoleDTO> list = new ArrayList<UserRoleDTO>( entityList.size() );
        for ( UserRole userRole : entityList ) {
            list.add( toDTO( userRole ) );
        }

        return list;
    }

    @Override
    public List<UserRole> toEntityList(List<UserRoleDTO> dtoList) {
        if ( dtoList == null ) {
            return null;
        }

        List<UserRole> list = new ArrayList<UserRole>( dtoList.size() );
        for ( UserRoleDTO userRoleDTO : dtoList ) {
            list.add( toEntity( userRoleDTO ) );
        }

        return list;
    }
}
