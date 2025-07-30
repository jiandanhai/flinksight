package com.flinksight.backend.mapper;

import com.flinksight.backend.domain.JobLog;
import com.flinksight.common.dto.JobLogDTO;
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
public class JobLogStructMapperImpl implements JobLogStructMapper {

    @Override
    public JobLogDTO toDTO(JobLog entity) {
        if ( entity == null ) {
            return null;
        }

        JobLogDTO.JobLogDTOBuilder jobLogDTO = JobLogDTO.builder();

        jobLogDTO.id( entity.getId() );
        jobLogDTO.tenantId( entity.getTenantId() );
        jobLogDTO.jobId( entity.getJobId() );
        jobLogDTO.level( entity.getLevel() );
        jobLogDTO.content( entity.getContent() );
        jobLogDTO.ts( entity.getTs() );
        jobLogDTO.isDeleted( entity.getIsDeleted() );

        return jobLogDTO.build();
    }

    @Override
    public JobLog toEntity(JobLogDTO dto) {
        if ( dto == null ) {
            return null;
        }

        JobLog.JobLogBuilder jobLog = JobLog.builder();

        jobLog.id( dto.getId() );
        jobLog.tenantId( dto.getTenantId() );
        jobLog.jobId( dto.getJobId() );
        jobLog.level( dto.getLevel() );
        jobLog.content( dto.getContent() );
        jobLog.ts( dto.getTs() );
        jobLog.isDeleted( dto.getIsDeleted() );

        return jobLog.build();
    }

    @Override
    public List<JobLogDTO> toDTOList(List<JobLog> entityList) {
        if ( entityList == null ) {
            return null;
        }

        List<JobLogDTO> list = new ArrayList<JobLogDTO>( entityList.size() );
        for ( JobLog jobLog : entityList ) {
            list.add( toDTO( jobLog ) );
        }

        return list;
    }

    @Override
    public List<JobLog> toEntityList(List<JobLogDTO> dtoList) {
        if ( dtoList == null ) {
            return null;
        }

        List<JobLog> list = new ArrayList<JobLog>( dtoList.size() );
        for ( JobLogDTO jobLogDTO : dtoList ) {
            list.add( toEntity( jobLogDTO ) );
        }

        return list;
    }
}
