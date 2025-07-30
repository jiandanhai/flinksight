package com.flinksight.backend.mapper;

import com.flinksight.backend.domain.Tag;
import com.flinksight.common.dto.TagDTO;
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
public class TagStructMapperImpl implements TagStructMapper {

    @Override
    public TagDTO toDTO(Tag entity) {
        if ( entity == null ) {
            return null;
        }

        TagDTO.TagDTOBuilder tagDTO = TagDTO.builder();

        tagDTO.id( entity.getId() );
        tagDTO.name( entity.getName() );
        tagDTO.type( entity.getType() );
        tagDTO.color( entity.getColor() );
        tagDTO.tenantId( entity.getTenantId() );
        tagDTO.isDeleted( entity.getIsDeleted() );

        return tagDTO.build();
    }

    @Override
    public Tag toEntity(TagDTO dto) {
        if ( dto == null ) {
            return null;
        }

        Tag.TagBuilder tag = Tag.builder();

        tag.id( dto.getId() );
        tag.name( dto.getName() );
        tag.color( dto.getColor() );
        tag.type( dto.getType() );
        tag.tenantId( dto.getTenantId() );
        tag.isDeleted( dto.getIsDeleted() );

        return tag.build();
    }

    @Override
    public List<TagDTO> toDTOList(List<Tag> entityList) {
        if ( entityList == null ) {
            return null;
        }

        List<TagDTO> list = new ArrayList<TagDTO>( entityList.size() );
        for ( Tag tag : entityList ) {
            list.add( toDTO( tag ) );
        }

        return list;
    }

    @Override
    public List<Tag> toEntityList(List<TagDTO> dtoList) {
        if ( dtoList == null ) {
            return null;
        }

        List<Tag> list = new ArrayList<Tag>( dtoList.size() );
        for ( TagDTO tagDTO : dtoList ) {
            list.add( toEntity( tagDTO ) );
        }

        return list;
    }
}
