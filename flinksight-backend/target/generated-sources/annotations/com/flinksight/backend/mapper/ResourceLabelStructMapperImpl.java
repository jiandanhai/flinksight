package com.flinksight.backend.mapper;

import com.flinksight.backend.domain.ResourceLabel;
import com.flinksight.common.dto.ResourceLabelDTO;
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
public class ResourceLabelStructMapperImpl implements ResourceLabelStructMapper {

    @Override
    public ResourceLabelDTO toDTO(ResourceLabel entity) {
        if ( entity == null ) {
            return null;
        }

        ResourceLabelDTO.ResourceLabelDTOBuilder resourceLabelDTO = ResourceLabelDTO.builder();

        resourceLabelDTO.id( entity.getId() );
        resourceLabelDTO.resourceId( entity.getResourceId() );
        resourceLabelDTO.labelId( entity.getLabelId() );
        resourceLabelDTO.isDeleted( entity.getIsDeleted() );

        return resourceLabelDTO.build();
    }

    @Override
    public ResourceLabel toEntity(ResourceLabelDTO dto) {
        if ( dto == null ) {
            return null;
        }

        ResourceLabel.ResourceLabelBuilder resourceLabel = ResourceLabel.builder();

        resourceLabel.id( dto.getId() );
        resourceLabel.resourceId( dto.getResourceId() );
        resourceLabel.labelId( dto.getLabelId() );
        resourceLabel.isDeleted( dto.getIsDeleted() );

        return resourceLabel.build();
    }

    @Override
    public List<ResourceLabelDTO> toDTOList(List<ResourceLabel> entityList) {
        if ( entityList == null ) {
            return null;
        }

        List<ResourceLabelDTO> list = new ArrayList<ResourceLabelDTO>( entityList.size() );
        for ( ResourceLabel resourceLabel : entityList ) {
            list.add( toDTO( resourceLabel ) );
        }

        return list;
    }

    @Override
    public List<ResourceLabel> toEntityList(List<ResourceLabelDTO> dtoList) {
        if ( dtoList == null ) {
            return null;
        }

        List<ResourceLabel> list = new ArrayList<ResourceLabel>( dtoList.size() );
        for ( ResourceLabelDTO resourceLabelDTO : dtoList ) {
            list.add( toEntity( resourceLabelDTO ) );
        }

        return list;
    }
}
