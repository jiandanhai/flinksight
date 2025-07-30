package com.flinksight.backend.mapper;

import com.flinksight.backend.domain.Job;
import com.flinksight.common.dto.JobDTO;
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
public class JobStructMapperImpl implements JobStructMapper {

    @Override
    public JobDTO toDTO(Job entity) {
        if ( entity == null ) {
            return null;
        }

        JobDTO.JobDTOBuilder jobDTO = JobDTO.builder();

        jobDTO.id( entity.getId() );
        jobDTO.tenantId( entity.getTenantId() );
        jobDTO.clusterId( entity.getClusterId() );
        jobDTO.name( entity.getName() );
        jobDTO.type( entity.getType() );
        jobDTO.status( entity.getStatus() );
        jobDTO.ownerId( entity.getOwnerId() );
        jobDTO.startTime( entity.getStartTime() );
        jobDTO.endTime( entity.getEndTime() );
        jobDTO.isDeleted( entity.getIsDeleted() );
        jobDTO.createTime( entity.getCreateTime() );
        jobDTO.updateTime( entity.getUpdateTime() );

        return jobDTO.build();
    }

    @Override
    public Job toEntity(JobDTO dto) {
        if ( dto == null ) {
            return null;
        }

        Job.JobBuilder job = Job.builder();

        job.id( dto.getId() );
        job.tenantId( dto.getTenantId() );
        job.clusterId( dto.getClusterId() );
        job.name( dto.getName() );
        job.type( dto.getType() );
        job.status( dto.getStatus() );
        job.ownerId( dto.getOwnerId() );
        job.startTime( dto.getStartTime() );
        job.endTime( dto.getEndTime() );
        job.isDeleted( dto.getIsDeleted() );
        job.createTime( dto.getCreateTime() );
        job.updateTime( dto.getUpdateTime() );

        return job.build();
    }

    @Override
    public List<JobDTO> toDTOList(List<Job> entityList) {
        if ( entityList == null ) {
            return null;
        }

        List<JobDTO> list = new ArrayList<JobDTO>( entityList.size() );
        for ( Job job : entityList ) {
            list.add( toDTO( job ) );
        }

        return list;
    }

    @Override
    public List<Job> toEntityList(List<JobDTO> dtoList) {
        if ( dtoList == null ) {
            return null;
        }

        List<Job> list = new ArrayList<Job>( dtoList.size() );
        for ( JobDTO jobDTO : dtoList ) {
            list.add( toEntity( jobDTO ) );
        }

        return list;
    }
}
