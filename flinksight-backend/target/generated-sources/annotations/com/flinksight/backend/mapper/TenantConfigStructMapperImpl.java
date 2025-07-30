package com.flinksight.backend.mapper;

import com.flinksight.backend.domain.TenantConfig;
import com.flinksight.common.dto.TenantConfigDTO;
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
public class TenantConfigStructMapperImpl implements TenantConfigStructMapper {

    @Override
    public TenantConfigDTO toDTO(TenantConfig entity) {
        if ( entity == null ) {
            return null;
        }

        TenantConfigDTO.TenantConfigDTOBuilder tenantConfigDTO = TenantConfigDTO.builder();

        tenantConfigDTO.id( entity.getId() );
        tenantConfigDTO.tenantId( entity.getTenantId() );
        tenantConfigDTO.configKey( entity.getConfigKey() );
        tenantConfigDTO.configValue( entity.getConfigValue() );
        tenantConfigDTO.description( entity.getDescription() );
        tenantConfigDTO.isDeleted( entity.getIsDeleted() );
        tenantConfigDTO.createTime( entity.getCreateTime() );
        tenantConfigDTO.updateTime( entity.getUpdateTime() );

        return tenantConfigDTO.build();
    }

    @Override
    public TenantConfig toEntity(TenantConfigDTO dto) {
        if ( dto == null ) {
            return null;
        }

        TenantConfig.TenantConfigBuilder tenantConfig = TenantConfig.builder();

        tenantConfig.id( dto.getId() );
        tenantConfig.tenantId( dto.getTenantId() );
        tenantConfig.configKey( dto.getConfigKey() );
        tenantConfig.configValue( dto.getConfigValue() );
        tenantConfig.description( dto.getDescription() );
        tenantConfig.isDeleted( dto.getIsDeleted() );
        tenantConfig.createTime( dto.getCreateTime() );
        tenantConfig.updateTime( dto.getUpdateTime() );

        return tenantConfig.build();
    }

    @Override
    public List<TenantConfigDTO> toDTOList(List<TenantConfig> entityList) {
        if ( entityList == null ) {
            return null;
        }

        List<TenantConfigDTO> list = new ArrayList<TenantConfigDTO>( entityList.size() );
        for ( TenantConfig tenantConfig : entityList ) {
            list.add( toDTO( tenantConfig ) );
        }

        return list;
    }

    @Override
    public List<TenantConfig> toEntityList(List<TenantConfigDTO> dtoList) {
        if ( dtoList == null ) {
            return null;
        }

        List<TenantConfig> list = new ArrayList<TenantConfig>( dtoList.size() );
        for ( TenantConfigDTO tenantConfigDTO : dtoList ) {
            list.add( toEntity( tenantConfigDTO ) );
        }

        return list;
    }
}
