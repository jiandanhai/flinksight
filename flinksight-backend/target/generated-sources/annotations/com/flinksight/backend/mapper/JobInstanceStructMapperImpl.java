package com.flinksight.backend.mapper;

import com.flinksight.backend.domain.JobInstance;
import com.flinksight.common.dto.JobInstanceDTO;
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
public class JobInstanceStructMapperImpl implements JobInstanceStructMapper {

    @Override
    public JobInstanceDTO toDTO(JobInstance entity) {
        if ( entity == null ) {
            return null;
        }

        JobInstanceDTO jobInstanceDTO = new JobInstanceDTO();

        jobInstanceDTO.setId( entity.getId() );
        jobInstanceDTO.setJobId( entity.getJobId() );
        jobInstanceDTO.setStartTime( entity.getStartTime() );
        jobInstanceDTO.setEndTime( entity.getEndTime() );
        if ( entity.getStatus() != null ) {
            jobInstanceDTO.setStatus( Integer.parseInt( entity.getStatus() ) );
        }
        jobInstanceDTO.setTenantId( entity.getTenantId() );
        jobInstanceDTO.setIsDeleted( entity.getIsDeleted() );

        return jobInstanceDTO;
    }

    @Override
    public JobInstance toEntity(JobInstanceDTO dto) {
        if ( dto == null ) {
            return null;
        }

        JobInstance.JobInstanceBuilder jobInstance = JobInstance.builder();

        jobInstance.id( dto.getId() );
        jobInstance.jobId( dto.getJobId() );
        if ( dto.getStatus() != null ) {
            jobInstance.status( String.valueOf( dto.getStatus() ) );
        }
        jobInstance.startTime( dto.getStartTime() );
        jobInstance.endTime( dto.getEndTime() );
        jobInstance.isDeleted( dto.getIsDeleted() );
        jobInstance.tenantId( dto.getTenantId() );

        return jobInstance.build();
    }

    @Override
    public List<JobInstanceDTO> toDTOList(List<JobInstance> entityList) {
        if ( entityList == null ) {
            return null;
        }

        List<JobInstanceDTO> list = new ArrayList<JobInstanceDTO>( entityList.size() );
        for ( JobInstance jobInstance : entityList ) {
            list.add( toDTO( jobInstance ) );
        }

        return list;
    }

    @Override
    public List<JobInstance> toEntityList(List<JobInstanceDTO> dtoList) {
        if ( dtoList == null ) {
            return null;
        }

        List<JobInstance> list = new ArrayList<JobInstance>( dtoList.size() );
        for ( JobInstanceDTO jobInstanceDTO : dtoList ) {
            list.add( toEntity( jobInstanceDTO ) );
        }

        return list;
    }
}
