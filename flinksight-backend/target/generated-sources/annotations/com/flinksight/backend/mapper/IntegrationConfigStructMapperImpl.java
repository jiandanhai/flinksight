package com.flinksight.backend.mapper;

import com.flinksight.backend.domain.IntegrationConfig;
import com.flinksight.common.dto.IntegrationConfigDTO;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2025-07-30T19:25:54+0800",
    comments = "version: 1.5.5.Final, compiler: javac, environment: Java 17.0.13 (Oracle Corporation)"
)
@Component
public class IntegrationConfigStructMapperImpl implements IntegrationConfigStructMapper {

    @Override
    public IntegrationConfigDTO toDTO(IntegrationConfig entity) {
        if ( entity == null ) {
            return null;
        }

        IntegrationConfigDTO.IntegrationConfigDTOBuilder integrationConfigDTO = IntegrationConfigDTO.builder();

        integrationConfigDTO.id( entity.getId() );
        integrationConfigDTO.name( entity.getName() );
        integrationConfigDTO.type( entity.getType() );
        integrationConfigDTO.configJson( entity.getConfigJson() );
        integrationConfigDTO.tenantId( entity.getTenantId() );
        integrationConfigDTO.status( entity.getStatus() );
        integrationConfigDTO.createTime( entity.getCreateTime() );
        integrationConfigDTO.isDeleted( entity.getIsDeleted() );

        return integrationConfigDTO.build();
    }

    @Override
    public IntegrationConfig toEntity(IntegrationConfigDTO dto) {
        if ( dto == null ) {
            return null;
        }

        IntegrationConfig.IntegrationConfigBuilder integrationConfig = IntegrationConfig.builder();

        integrationConfig.id( dto.getId() );
        integrationConfig.name( dto.getName() );
        integrationConfig.type( dto.getType() );
        integrationConfig.configJson( dto.getConfigJson() );
        integrationConfig.tenantId( dto.getTenantId() );
        integrationConfig.status( dto.getStatus() );
        integrationConfig.createTime( dto.getCreateTime() );
        integrationConfig.isDeleted( dto.getIsDeleted() );

        return integrationConfig.build();
    }

    @Override
    public List<IntegrationConfigDTO> toDTOList(List<IntegrationConfig> entityList) {
        if ( entityList == null ) {
            return null;
        }

        List<IntegrationConfigDTO> list = new ArrayList<IntegrationConfigDTO>( entityList.size() );
        for ( IntegrationConfig integrationConfig : entityList ) {
            list.add( toDTO( integrationConfig ) );
        }

        return list;
    }

    @Override
    public List<IntegrationConfig> toEntityList(List<IntegrationConfigDTO> dtoList) {
        if ( dtoList == null ) {
            return null;
        }

        List<IntegrationConfig> list = new ArrayList<IntegrationConfig>( dtoList.size() );
        for ( IntegrationConfigDTO integrationConfigDTO : dtoList ) {
            list.add( toEntity( integrationConfigDTO ) );
        }

        return list;
    }
}
