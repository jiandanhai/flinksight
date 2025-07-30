package com.flinksight.backend.mapper;

import com.flinksight.backend.domain.RoleMenu;
import com.flinksight.common.dto.RoleMenuDTO;
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
public class RoleMenuStructMapperImpl implements RoleMenuStructMapper {

    @Override
    public RoleMenuDTO toDTO(RoleMenu entity) {
        if ( entity == null ) {
            return null;
        }

        RoleMenuDTO.RoleMenuDTOBuilder roleMenuDTO = RoleMenuDTO.builder();

        roleMenuDTO.id( entity.getId() );
        roleMenuDTO.roleId( entity.getRoleId() );
        roleMenuDTO.menuId( entity.getMenuId() );
        roleMenuDTO.isDeleted( entity.getIsDeleted() );

        return roleMenuDTO.build();
    }

    @Override
    public RoleMenu toEntity(RoleMenuDTO dto) {
        if ( dto == null ) {
            return null;
        }

        RoleMenu.RoleMenuBuilder roleMenu = RoleMenu.builder();

        roleMenu.id( dto.getId() );
        roleMenu.roleId( dto.getRoleId() );
        roleMenu.menuId( dto.getMenuId() );
        roleMenu.isDeleted( dto.getIsDeleted() );

        return roleMenu.build();
    }

    @Override
    public List<RoleMenuDTO> toDTOList(List<RoleMenu> entityList) {
        if ( entityList == null ) {
            return null;
        }

        List<RoleMenuDTO> list = new ArrayList<RoleMenuDTO>( entityList.size() );
        for ( RoleMenu roleMenu : entityList ) {
            list.add( toDTO( roleMenu ) );
        }

        return list;
    }

    @Override
    public List<RoleMenu> toEntityList(List<RoleMenuDTO> dtoList) {
        if ( dtoList == null ) {
            return null;
        }

        List<RoleMenu> list = new ArrayList<RoleMenu>( dtoList.size() );
        for ( RoleMenuDTO roleMenuDTO : dtoList ) {
            list.add( toEntity( roleMenuDTO ) );
        }

        return list;
    }
}
