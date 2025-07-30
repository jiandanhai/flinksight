package com.flinksight.backend.mapper;

import com.flinksight.backend.domain.UserDepartment;
import com.flinksight.common.dto.UserDepartmentDTO;
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
public class UserDepartmentStructMapperImpl implements UserDepartmentStructMapper {

    @Override
    public UserDepartmentDTO toDTO(UserDepartment entity) {
        if ( entity == null ) {
            return null;
        }

        UserDepartmentDTO.UserDepartmentDTOBuilder userDepartmentDTO = UserDepartmentDTO.builder();

        userDepartmentDTO.id( entity.getId() );
        userDepartmentDTO.userId( entity.getUserId() );
        userDepartmentDTO.departmentId( entity.getDepartmentId() );
        userDepartmentDTO.isDeleted( entity.getIsDeleted() );

        return userDepartmentDTO.build();
    }

    @Override
    public UserDepartment toEntity(UserDepartmentDTO dto) {
        if ( dto == null ) {
            return null;
        }

        UserDepartment.UserDepartmentBuilder userDepartment = UserDepartment.builder();

        userDepartment.id( dto.getId() );
        userDepartment.userId( dto.getUserId() );
        userDepartment.departmentId( dto.getDepartmentId() );
        userDepartment.isDeleted( dto.getIsDeleted() );

        return userDepartment.build();
    }

    @Override
    public List<UserDepartmentDTO> toDTOList(List<UserDepartment> entityList) {
        if ( entityList == null ) {
            return null;
        }

        List<UserDepartmentDTO> list = new ArrayList<UserDepartmentDTO>( entityList.size() );
        for ( UserDepartment userDepartment : entityList ) {
            list.add( toDTO( userDepartment ) );
        }

        return list;
    }

    @Override
    public List<UserDepartment> toEntityList(List<UserDepartmentDTO> dtoList) {
        if ( dtoList == null ) {
            return null;
        }

        List<UserDepartment> list = new ArrayList<UserDepartment>( dtoList.size() );
        for ( UserDepartmentDTO userDepartmentDTO : dtoList ) {
            list.add( toEntity( userDepartmentDTO ) );
        }

        return list;
    }
}
