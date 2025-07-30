package com.flinksight.backend.mapper;

import com.flinksight.backend.domain.RoleDataScope;
import com.flinksight.common.dto.RoleDataScopeDTO;
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
public class RoleDataScopeStructMapperImpl implements RoleDataScopeStructMapper {

    @Override
    public RoleDataScopeDTO toDTO(RoleDataScope entity) {
        if ( entity == null ) {
            return null;
        }

        RoleDataScopeDTO.RoleDataScopeDTOBuilder roleDataScopeDTO = RoleDataScopeDTO.builder();

        roleDataScopeDTO.id( entity.getId() );
        roleDataScopeDTO.roleId( entity.getRoleId() );
        roleDataScopeDTO.dataScopeId( entity.getDataScopeId() );
        roleDataScopeDTO.isDeleted( entity.getIsDeleted() );

        return roleDataScopeDTO.build();
    }

    @Override
    public RoleDataScope toEntity(RoleDataScopeDTO dto) {
        if ( dto == null ) {
            return null;
        }

        RoleDataScope.RoleDataScopeBuilder roleDataScope = RoleDataScope.builder();

        roleDataScope.id( dto.getId() );
        roleDataScope.roleId( dto.getRoleId() );
        roleDataScope.dataScopeId( dto.getDataScopeId() );
        roleDataScope.isDeleted( dto.getIsDeleted() );

        return roleDataScope.build();
    }

    @Override
    public List<RoleDataScopeDTO> toDTOList(List<RoleDataScope> entityList) {
        if ( entityList == null ) {
            return null;
        }

        List<RoleDataScopeDTO> list = new ArrayList<RoleDataScopeDTO>( entityList.size() );
        for ( RoleDataScope roleDataScope : entityList ) {
            list.add( toDTO( roleDataScope ) );
        }

        return list;
    }

    @Override
    public List<RoleDataScope> toEntityList(List<RoleDataScopeDTO> dtoList) {
        if ( dtoList == null ) {
            return null;
        }

        List<RoleDataScope> list = new ArrayList<RoleDataScope>( dtoList.size() );
        for ( RoleDataScopeDTO roleDataScopeDTO : dtoList ) {
            list.add( toEntity( roleDataScopeDTO ) );
        }

        return list;
    }
}
