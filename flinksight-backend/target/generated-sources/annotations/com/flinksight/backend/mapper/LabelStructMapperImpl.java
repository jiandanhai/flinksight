package com.flinksight.backend.mapper;

import com.flinksight.backend.domain.Label;
import com.flinksight.common.dto.LabelDTO;
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
public class LabelStructMapperImpl implements LabelStructMapper {

    @Override
    public LabelDTO toDTO(Label entity) {
        if ( entity == null ) {
            return null;
        }

        LabelDTO.LabelDTOBuilder labelDTO = LabelDTO.builder();

        labelDTO.id( entity.getId() );
        labelDTO.name( entity.getName() );
        labelDTO.color( entity.getColor() );
        labelDTO.type( entity.getType() );
        labelDTO.tenantId( entity.getTenantId() );
        labelDTO.isDeleted( entity.getIsDeleted() );

        return labelDTO.build();
    }

    @Override
    public Label toEntity(LabelDTO dto) {
        if ( dto == null ) {
            return null;
        }

        Label.LabelBuilder label = Label.builder();

        label.id( dto.getId() );
        label.name( dto.getName() );
        label.color( dto.getColor() );
        label.type( dto.getType() );
        label.tenantId( dto.getTenantId() );
        label.isDeleted( dto.getIsDeleted() );

        return label.build();
    }

    @Override
    public List<LabelDTO> toDTOList(List<Label> entityList) {
        if ( entityList == null ) {
            return null;
        }

        List<LabelDTO> list = new ArrayList<LabelDTO>( entityList.size() );
        for ( Label label : entityList ) {
            list.add( toDTO( label ) );
        }

        return list;
    }

    @Override
    public List<Label> toEntityList(List<LabelDTO> dtoList) {
        if ( dtoList == null ) {
            return null;
        }

        List<Label> list = new ArrayList<Label>( dtoList.size() );
        for ( LabelDTO labelDTO : dtoList ) {
            list.add( toEntity( labelDTO ) );
        }

        return list;
    }
}
