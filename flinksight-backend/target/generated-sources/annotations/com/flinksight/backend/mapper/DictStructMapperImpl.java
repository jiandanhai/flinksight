package com.flinksight.backend.mapper;

import com.flinksight.backend.domain.Dict;
import com.flinksight.common.dto.DictDTO;
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
public class DictStructMapperImpl implements DictStructMapper {

    @Override
    public DictDTO toDTO(Dict entity) {
        if ( entity == null ) {
            return null;
        }

        DictDTO.DictDTOBuilder dictDTO = DictDTO.builder();

        dictDTO.id( entity.getId() );
        dictDTO.dictType( entity.getDictType() );
        dictDTO.dictKey( entity.getDictKey() );
        dictDTO.dictValue( entity.getDictValue() );
        dictDTO.sort( entity.getSort() );
        dictDTO.description( entity.getDescription() );
        dictDTO.isDeleted( entity.getIsDeleted() );

        return dictDTO.build();
    }

    @Override
    public Dict toEntity(DictDTO dto) {
        if ( dto == null ) {
            return null;
        }

        Dict.DictBuilder dict = Dict.builder();

        dict.id( dto.getId() );
        dict.dictType( dto.getDictType() );
        dict.dictKey( dto.getDictKey() );
        dict.dictValue( dto.getDictValue() );
        dict.sort( dto.getSort() );
        dict.description( dto.getDescription() );
        dict.isDeleted( dto.getIsDeleted() );

        return dict.build();
    }

    @Override
    public List<DictDTO> toDTOList(List<Dict> entityList) {
        if ( entityList == null ) {
            return null;
        }

        List<DictDTO> list = new ArrayList<DictDTO>( entityList.size() );
        for ( Dict dict : entityList ) {
            list.add( toDTO( dict ) );
        }

        return list;
    }

    @Override
    public List<Dict> toEntityList(List<DictDTO> dtoList) {
        if ( dtoList == null ) {
            return null;
        }

        List<Dict> list = new ArrayList<Dict>( dtoList.size() );
        for ( DictDTO dictDTO : dtoList ) {
            list.add( toEntity( dictDTO ) );
        }

        return list;
    }
}
