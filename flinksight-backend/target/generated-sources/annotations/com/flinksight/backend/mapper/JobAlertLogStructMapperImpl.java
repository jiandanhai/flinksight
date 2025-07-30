package com.flinksight.backend.mapper;

import com.flinksight.backend.domain.JobAlertLog;
import com.flinksight.common.dto.JobAlertLogDTO;
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
public class JobAlertLogStructMapperImpl implements JobAlertLogStructMapper {

    @Override
    public JobAlertLogDTO toDTO(JobAlertLog entity) {
        if ( entity == null ) {
            return null;
        }

        JobAlertLogDTO.JobAlertLogDTOBuilder jobAlertLogDTO = JobAlertLogDTO.builder();

        jobAlertLogDTO.id( entity.getId() );
        jobAlertLogDTO.jobId( entity.getJobId() );
        jobAlertLogDTO.alertType( entity.getAlertType() );
        jobAlertLogDTO.alertMsg( entity.getAlertMsg() );
        jobAlertLogDTO.tenantId( entity.getTenantId() );
        jobAlertLogDTO.isDeleted( entity.getIsDeleted() );

        return jobAlertLogDTO.build();
    }

    @Override
    public JobAlertLog toEntity(JobAlertLogDTO dto) {
        if ( dto == null ) {
            return null;
        }

        JobAlertLog.JobAlertLogBuilder jobAlertLog = JobAlertLog.builder();

        jobAlertLog.id( dto.getId() );
        jobAlertLog.tenantId( dto.getTenantId() );
        jobAlertLog.jobId( dto.getJobId() );
        jobAlertLog.alertType( dto.getAlertType() );
        jobAlertLog.alertMsg( dto.getAlertMsg() );
        jobAlertLog.isDeleted( dto.getIsDeleted() );

        return jobAlertLog.build();
    }

    @Override
    public List<JobAlertLogDTO> toDTOList(List<JobAlertLog> entityList) {
        if ( entityList == null ) {
            return null;
        }

        List<JobAlertLogDTO> list = new ArrayList<JobAlertLogDTO>( entityList.size() );
        for ( JobAlertLog jobAlertLog : entityList ) {
            list.add( toDTO( jobAlertLog ) );
        }

        return list;
    }

    @Override
    public List<JobAlertLog> toEntityList(List<JobAlertLogDTO> dtoList) {
        if ( dtoList == null ) {
            return null;
        }

        List<JobAlertLog> list = new ArrayList<JobAlertLog>( dtoList.size() );
        for ( JobAlertLogDTO jobAlertLogDTO : dtoList ) {
            list.add( toEntity( jobAlertLogDTO ) );
        }

        return list;
    }
}
