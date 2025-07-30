package com.flinksight.backend.mapper;

import com.flinksight.backend.domain.JobDependency;
import com.flinksight.common.dto.JobDependencyDTO;
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
public class JobDependencyStructMapperImpl implements JobDependencyStructMapper {

    @Override
    public JobDependencyDTO toDTO(JobDependency entity) {
        if ( entity == null ) {
            return null;
        }

        JobDependencyDTO.JobDependencyDTOBuilder jobDependencyDTO = JobDependencyDTO.builder();

        jobDependencyDTO.id( entity.getId() );
        jobDependencyDTO.jobId( entity.getJobId() );
        jobDependencyDTO.isDeleted( entity.getIsDeleted() );

        return jobDependencyDTO.build();
    }

    @Override
    public JobDependency toEntity(JobDependencyDTO dto) {
        if ( dto == null ) {
            return null;
        }

        JobDependency.JobDependencyBuilder jobDependency = JobDependency.builder();

        jobDependency.id( dto.getId() );
        jobDependency.jobId( dto.getJobId() );
        jobDependency.isDeleted( dto.getIsDeleted() );

        return jobDependency.build();
    }

    @Override
    public List<JobDependencyDTO> toDTOList(List<JobDependency> entityList) {
        if ( entityList == null ) {
            return null;
        }

        List<JobDependencyDTO> list = new ArrayList<JobDependencyDTO>( entityList.size() );
        for ( JobDependency jobDependency : entityList ) {
            list.add( toDTO( jobDependency ) );
        }

        return list;
    }

    @Override
    public List<JobDependency> toEntityList(List<JobDependencyDTO> dtoList) {
        if ( dtoList == null ) {
            return null;
        }

        List<JobDependency> list = new ArrayList<JobDependency>( dtoList.size() );
        for ( JobDependencyDTO jobDependencyDTO : dtoList ) {
            list.add( toEntity( jobDependencyDTO ) );
        }

        return list;
    }
}
