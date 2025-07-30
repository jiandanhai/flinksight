package com.flinksight.backend.mapper;

import com.flinksight.backend.domain.JobPermission;
import com.flinksight.common.dto.JobPermissionDTO;
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
public class JobPermissionStructMapperImpl implements JobPermissionStructMapper {

    @Override
    public JobPermissionDTO toDTO(JobPermission entity) {
        if ( entity == null ) {
            return null;
        }

        JobPermissionDTO.JobPermissionDTOBuilder jobPermissionDTO = JobPermissionDTO.builder();

        jobPermissionDTO.id( entity.getId() );
        jobPermissionDTO.jobId( entity.getJobId() );
        jobPermissionDTO.tenantId( entity.getTenantId() );
        jobPermissionDTO.userId( entity.getUserId() );
        jobPermissionDTO.permissionId( entity.getPermissionId() );

        return jobPermissionDTO.build();
    }

    @Override
    public JobPermission toEntity(JobPermissionDTO dto) {
        if ( dto == null ) {
            return null;
        }

        JobPermission.JobPermissionBuilder jobPermission = JobPermission.builder();

        jobPermission.id( dto.getId() );
        jobPermission.jobId( dto.getJobId() );
        jobPermission.tenantId( dto.getTenantId() );
        jobPermission.userId( dto.getUserId() );
        jobPermission.permissionId( dto.getPermissionId() );

        return jobPermission.build();
    }

    @Override
    public List<JobPermissionDTO> toDTOList(List<JobPermission> entityList) {
        if ( entityList == null ) {
            return null;
        }

        List<JobPermissionDTO> list = new ArrayList<JobPermissionDTO>( entityList.size() );
        for ( JobPermission jobPermission : entityList ) {
            list.add( toDTO( jobPermission ) );
        }

        return list;
    }

    @Override
    public List<JobPermission> toEntityList(List<JobPermissionDTO> dtoList) {
        if ( dtoList == null ) {
            return null;
        }

        List<JobPermission> list = new ArrayList<JobPermission>( dtoList.size() );
        for ( JobPermissionDTO jobPermissionDTO : dtoList ) {
            list.add( toEntity( jobPermissionDTO ) );
        }

        return list;
    }
}
