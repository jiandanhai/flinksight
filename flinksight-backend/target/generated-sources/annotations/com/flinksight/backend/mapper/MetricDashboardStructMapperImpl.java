package com.flinksight.backend.mapper;

import com.flinksight.backend.domain.MetricDashboard;
import com.flinksight.common.dto.MetricDashboardDTO;
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
public class MetricDashboardStructMapperImpl implements MetricDashboardStructMapper {

    @Override
    public MetricDashboardDTO toDTO(MetricDashboard entity) {
        if ( entity == null ) {
            return null;
        }

        MetricDashboardDTO.MetricDashboardDTOBuilder metricDashboardDTO = MetricDashboardDTO.builder();

        metricDashboardDTO.id( entity.getId() );
        metricDashboardDTO.name( entity.getName() );
        metricDashboardDTO.tenantId( entity.getTenantId() );
        metricDashboardDTO.isDeleted( entity.getIsDeleted() );
        metricDashboardDTO.createTime( entity.getCreateTime() );

        return metricDashboardDTO.build();
    }

    @Override
    public MetricDashboard toEntity(MetricDashboardDTO dto) {
        if ( dto == null ) {
            return null;
        }

        MetricDashboard.MetricDashboardBuilder metricDashboard = MetricDashboard.builder();

        metricDashboard.id( dto.getId() );
        metricDashboard.name( dto.getName() );
        metricDashboard.tenantId( dto.getTenantId() );
        metricDashboard.isDeleted( dto.getIsDeleted() );
        metricDashboard.createTime( dto.getCreateTime() );

        return metricDashboard.build();
    }

    @Override
    public List<MetricDashboardDTO> toDTOList(List<MetricDashboard> entityList) {
        if ( entityList == null ) {
            return null;
        }

        List<MetricDashboardDTO> list = new ArrayList<MetricDashboardDTO>( entityList.size() );
        for ( MetricDashboard metricDashboard : entityList ) {
            list.add( toDTO( metricDashboard ) );
        }

        return list;
    }

    @Override
    public List<MetricDashboard> toEntityList(List<MetricDashboardDTO> dtoList) {
        if ( dtoList == null ) {
            return null;
        }

        List<MetricDashboard> list = new ArrayList<MetricDashboard>( dtoList.size() );
        for ( MetricDashboardDTO metricDashboardDTO : dtoList ) {
            list.add( toEntity( metricDashboardDTO ) );
        }

        return list;
    }
}
