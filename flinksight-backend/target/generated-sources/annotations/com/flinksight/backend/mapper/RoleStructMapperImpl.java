package com.flinksight.backend.mapper;

import com.flinksight.backend.domain.Role;
import com.flinksight.common.dto.RoleDTO;
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
public class RoleStructMapperImpl implements RoleStructMapper {

    @Override
    public RoleDTO toDTO(Role entity) {
        if ( entity == null ) {
            return null;
        }

        RoleDTO.RoleDTOBuilder roleDTO = RoleDTO.builder();

        roleDTO.id( entity.getId() );
        roleDTO.name( entity.getName() );
        roleDTO.code( entity.getCode() );
        roleDTO.desc( entity.getDesc() );
        roleDTO.isDeleted( entity.getIsDeleted() );

        return roleDTO.build();
    }

    @Override
    public Role toEntity(RoleDTO dto) {
        if ( dto == null ) {
            return null;
        }

        Role.RoleBuilder role = Role.builder();

        role.id( dto.getId() );
        role.name( dto.getName() );
        role.code( dto.getCode() );
        role.desc( dto.getDesc() );
        role.isDeleted( dto.getIsDeleted() );

        return role.build();
    }

    @Override
    public List<RoleDTO> toDTOList(List<Role> entityList) {
        if ( entityList == null ) {
            return null;
        }

        List<RoleDTO> list = new ArrayList<RoleDTO>( entityList.size() );
        for ( Role role : entityList ) {
            list.add( toDTO( role ) );
        }

        return list;
    }

    @Override
    public List<Role> toEntityList(List<RoleDTO> dtoList) {
        if ( dtoList == null ) {
            return null;
        }

        List<Role> list = new ArrayList<Role>( dtoList.size() );
        for ( RoleDTO roleDTO : dtoList ) {
            list.add( toEntity( roleDTO ) );
        }

        return list;
    }
}
