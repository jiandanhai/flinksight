package com.flinksight.backend.mapper;

import com.flinksight.backend.domain.DataSource;
import com.flinksight.common.dto.DataSourceDTO;
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
public class DataSourceStructMapperImpl implements DataSourceStructMapper {

    @Override
    public DataSourceDTO toDTO(DataSource entity) {
        if ( entity == null ) {
            return null;
        }

        DataSourceDTO.DataSourceDTOBuilder dataSourceDTO = DataSourceDTO.builder();

        dataSourceDTO.id( entity.getId() );
        dataSourceDTO.name( entity.getName() );
        dataSourceDTO.type( entity.getType() );
        dataSourceDTO.connectInfo( entity.getConnectInfo() );
        dataSourceDTO.tenantId( entity.getTenantId() );
        dataSourceDTO.description( entity.getDescription() );
        dataSourceDTO.isDeleted( entity.getIsDeleted() );
        dataSourceDTO.createTime( entity.getCreateTime() );

        return dataSourceDTO.build();
    }

    @Override
    public DataSource toEntity(DataSourceDTO dto) {
        if ( dto == null ) {
            return null;
        }

        DataSource.DataSourceBuilder dataSource = DataSource.builder();

        dataSource.id( dto.getId() );
        dataSource.name( dto.getName() );
        dataSource.type( dto.getType() );
        dataSource.connectInfo( dto.getConnectInfo() );
        dataSource.tenantId( dto.getTenantId() );
        dataSource.description( dto.getDescription() );
        dataSource.isDeleted( dto.getIsDeleted() );
        dataSource.createTime( dto.getCreateTime() );

        return dataSource.build();
    }

    @Override
    public List<DataSourceDTO> toDTOList(List<DataSource> entityList) {
        if ( entityList == null ) {
            return null;
        }

        List<DataSourceDTO> list = new ArrayList<DataSourceDTO>( entityList.size() );
        for ( DataSource dataSource : entityList ) {
            list.add( toDTO( dataSource ) );
        }

        return list;
    }

    @Override
    public List<DataSource> toEntityList(List<DataSourceDTO> dtoList) {
        if ( dtoList == null ) {
            return null;
        }

        List<DataSource> list = new ArrayList<DataSource>( dtoList.size() );
        for ( DataSourceDTO dataSourceDTO : dtoList ) {
            list.add( toEntity( dataSourceDTO ) );
        }

        return list;
    }
}
