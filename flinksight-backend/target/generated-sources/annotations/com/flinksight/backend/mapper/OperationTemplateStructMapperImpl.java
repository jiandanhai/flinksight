package com.flinksight.backend.mapper;

import com.flinksight.backend.domain.OperationTemplate;
import com.flinksight.common.dto.OperationTemplateDTO;
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
public class OperationTemplateStructMapperImpl implements OperationTemplateStructMapper {

    @Override
    public OperationTemplateDTO toDTO(OperationTemplate entity) {
        if ( entity == null ) {
            return null;
        }

        OperationTemplateDTO.OperationTemplateDTOBuilder operationTemplateDTO = OperationTemplateDTO.builder();

        operationTemplateDTO.id( entity.getId() );
        operationTemplateDTO.name( entity.getName() );
        operationTemplateDTO.type( entity.getType() );
        operationTemplateDTO.content( entity.getContent() );
        operationTemplateDTO.tenantId( entity.getTenantId() );
        operationTemplateDTO.createTime( entity.getCreateTime() );
        operationTemplateDTO.isDeleted( entity.getIsDeleted() );

        return operationTemplateDTO.build();
    }

    @Override
    public OperationTemplate toEntity(OperationTemplateDTO dto) {
        if ( dto == null ) {
            return null;
        }

        OperationTemplate.OperationTemplateBuilder operationTemplate = OperationTemplate.builder();

        operationTemplate.id( dto.getId() );
        operationTemplate.name( dto.getName() );
        operationTemplate.type( dto.getType() );
        operationTemplate.content( dto.getContent() );
        operationTemplate.tenantId( dto.getTenantId() );
        operationTemplate.createTime( dto.getCreateTime() );
        operationTemplate.isDeleted( dto.getIsDeleted() );

        return operationTemplate.build();
    }

    @Override
    public List<OperationTemplateDTO> toDTOList(List<OperationTemplate> entityList) {
        if ( entityList == null ) {
            return null;
        }

        List<OperationTemplateDTO> list = new ArrayList<OperationTemplateDTO>( entityList.size() );
        for ( OperationTemplate operationTemplate : entityList ) {
            list.add( toDTO( operationTemplate ) );
        }

        return list;
    }

    @Override
    public List<OperationTemplate> toEntityList(List<OperationTemplateDTO> dtoList) {
        if ( dtoList == null ) {
            return null;
        }

        List<OperationTemplate> list = new ArrayList<OperationTemplate>( dtoList.size() );
        for ( OperationTemplateDTO operationTemplateDTO : dtoList ) {
            list.add( toEntity( operationTemplateDTO ) );
        }

        return list;
    }
}
