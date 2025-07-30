package com.flinksight.backend.mapper;

import com.flinksight.backend.domain.JobMetric;
import com.flinksight.common.dto.JobMetricDTO;
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
public class JobMetricStructMapperImpl implements JobMetricStructMapper {

    @Override
    public JobMetricDTO toDTO(JobMetric entity) {
        if ( entity == null ) {
            return null;
        }

        JobMetricDTO.JobMetricDTOBuilder jobMetricDTO = JobMetricDTO.builder();

        jobMetricDTO.id( entity.getId() );
        jobMetricDTO.tenantId( entity.getTenantId() );
        jobMetricDTO.jobId( entity.getJobId() );
        jobMetricDTO.metricKey( entity.getMetricKey() );
        jobMetricDTO.value( entity.getValue() );
        jobMetricDTO.ts( entity.getTs() );
        jobMetricDTO.isDeleted( entity.getIsDeleted() );

        return jobMetricDTO.build();
    }

    @Override
    public JobMetric toEntity(JobMetricDTO dto) {
        if ( dto == null ) {
            return null;
        }

        JobMetric.JobMetricBuilder jobMetric = JobMetric.builder();

        jobMetric.id( dto.getId() );
        jobMetric.tenantId( dto.getTenantId() );
        jobMetric.jobId( dto.getJobId() );
        jobMetric.metricKey( dto.getMetricKey() );
        jobMetric.value( dto.getValue() );
        jobMetric.ts( dto.getTs() );
        jobMetric.isDeleted( dto.getIsDeleted() );

        return jobMetric.build();
    }

    @Override
    public List<JobMetricDTO> toDTOList(List<JobMetric> entityList) {
        if ( entityList == null ) {
            return null;
        }

        List<JobMetricDTO> list = new ArrayList<JobMetricDTO>( entityList.size() );
        for ( JobMetric jobMetric : entityList ) {
            list.add( toDTO( jobMetric ) );
        }

        return list;
    }

    @Override
    public List<JobMetric> toEntityList(List<JobMetricDTO> dtoList) {
        if ( dtoList == null ) {
            return null;
        }

        List<JobMetric> list = new ArrayList<JobMetric>( dtoList.size() );
        for ( JobMetricDTO jobMetricDTO : dtoList ) {
            list.add( toEntity( jobMetricDTO ) );
        }

        return list;
    }
}
