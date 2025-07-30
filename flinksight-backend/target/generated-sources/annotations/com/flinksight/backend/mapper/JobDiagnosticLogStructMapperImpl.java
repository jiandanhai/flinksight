package com.flinksight.backend.mapper;

import com.flinksight.backend.domain.JobDiagnosticLog;
import com.flinksight.common.dto.JobDiagnosticLogDTO;
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
public class JobDiagnosticLogStructMapperImpl implements JobDiagnosticLogStructMapper {

    @Override
    public JobDiagnosticLogDTO toDTO(JobDiagnosticLog entity) {
        if ( entity == null ) {
            return null;
        }

        JobDiagnosticLogDTO.JobDiagnosticLogDTOBuilder jobDiagnosticLogDTO = JobDiagnosticLogDTO.builder();

        jobDiagnosticLogDTO.id( entity.getId() );
        jobDiagnosticLogDTO.jobId( entity.getJobId() );
        jobDiagnosticLogDTO.jobName( entity.getJobName() );
        jobDiagnosticLogDTO.logTime( entity.getLogTime() );
        jobDiagnosticLogDTO.traceId( entity.getTraceId() );
        jobDiagnosticLogDTO.isDeleted( entity.getIsDeleted() );

        return jobDiagnosticLogDTO.build();
    }

    @Override
    public JobDiagnosticLog toEntity(JobDiagnosticLogDTO dto) {
        if ( dto == null ) {
            return null;
        }

        JobDiagnosticLog.JobDiagnosticLogBuilder jobDiagnosticLog = JobDiagnosticLog.builder();

        jobDiagnosticLog.id( dto.getId() );
        jobDiagnosticLog.jobId( dto.getJobId() );
        jobDiagnosticLog.jobName( dto.getJobName() );
        jobDiagnosticLog.logTime( dto.getLogTime() );
        jobDiagnosticLog.traceId( dto.getTraceId() );
        jobDiagnosticLog.isDeleted( dto.getIsDeleted() );

        return jobDiagnosticLog.build();
    }

    @Override
    public List<JobDiagnosticLogDTO> toDTOList(List<JobDiagnosticLog> entityList) {
        if ( entityList == null ) {
            return null;
        }

        List<JobDiagnosticLogDTO> list = new ArrayList<JobDiagnosticLogDTO>( entityList.size() );
        for ( JobDiagnosticLog jobDiagnosticLog : entityList ) {
            list.add( toDTO( jobDiagnosticLog ) );
        }

        return list;
    }

    @Override
    public List<JobDiagnosticLog> toEntityList(List<JobDiagnosticLogDTO> dtoList) {
        if ( dtoList == null ) {
            return null;
        }

        List<JobDiagnosticLog> list = new ArrayList<JobDiagnosticLog>( dtoList.size() );
        for ( JobDiagnosticLogDTO jobDiagnosticLogDTO : dtoList ) {
            list.add( toEntity( jobDiagnosticLogDTO ) );
        }

        return list;
    }
}
