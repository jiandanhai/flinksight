package com.flinksight.backend.mapper;

import com.flinksight.backend.domain.DeptRole;
import com.flinksight.common.dto.DeptRoleDTO;
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
public class DeptRoleStructMapperImpl implements DeptRoleStructMapper {

    @Override
    public DeptRoleDTO toDTO(DeptRole entity) {
        if ( entity == null ) {
            return null;
        }

        DeptRoleDTO.DeptRoleDTOBuilder deptRoleDTO = DeptRoleDTO.builder();

        deptRoleDTO.id( entity.getId() );
        deptRoleDTO.deptId( entity.getDeptId() );
        deptRoleDTO.roleId( entity.getRoleId() );
        deptRoleDTO.isDeleted( entity.getIsDeleted() );

        return deptRoleDTO.build();
    }

    @Override
    public DeptRole toEntity(DeptRoleDTO dto) {
        if ( dto == null ) {
            return null;
        }

        DeptRole.DeptRoleBuilder deptRole = DeptRole.builder();

        deptRole.id( dto.getId() );
        deptRole.deptId( dto.getDeptId() );
        deptRole.roleId( dto.getRoleId() );
        deptRole.isDeleted( dto.getIsDeleted() );

        return deptRole.build();
    }

    @Override
    public List<DeptRoleDTO> toDTOList(List<DeptRole> entityList) {
        if ( entityList == null ) {
            return null;
        }

        List<DeptRoleDTO> list = new ArrayList<DeptRoleDTO>( entityList.size() );
        for ( DeptRole deptRole : entityList ) {
            list.add( toDTO( deptRole ) );
        }

        return list;
    }

    @Override
    public List<DeptRole> toEntityList(List<DeptRoleDTO> dtoList) {
        if ( dtoList == null ) {
            return null;
        }

        List<DeptRole> list = new ArrayList<DeptRole>( dtoList.size() );
        for ( DeptRoleDTO deptRoleDTO : dtoList ) {
            list.add( toEntity( deptRoleDTO ) );
        }

        return list;
    }
}
