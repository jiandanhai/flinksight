package com.flinksight.backend.mapper;

import com.flinksight.backend.domain.File;
import com.flinksight.common.dto.FileDTO;
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
public class FileStructMapperImpl implements FileStructMapper {

    @Override
    public FileDTO toDTO(File entity) {
        if ( entity == null ) {
            return null;
        }

        FileDTO.FileDTOBuilder fileDTO = FileDTO.builder();

        fileDTO.id( entity.getId() );
        fileDTO.tenantId( entity.getTenantId() );
        fileDTO.isDeleted( entity.getIsDeleted() );

        return fileDTO.build();
    }

    @Override
    public File toEntity(FileDTO dto) {
        if ( dto == null ) {
            return null;
        }

        File.FileBuilder file = File.builder();

        file.id( dto.getId() );
        file.tenantId( dto.getTenantId() );
        file.isDeleted( dto.getIsDeleted() );

        return file.build();
    }

    @Override
    public List<FileDTO> toDTOList(List<File> entityList) {
        if ( entityList == null ) {
            return null;
        }

        List<FileDTO> list = new ArrayList<FileDTO>( entityList.size() );
        for ( File file : entityList ) {
            list.add( toDTO( file ) );
        }

        return list;
    }

    @Override
    public List<File> toEntityList(List<FileDTO> dtoList) {
        if ( dtoList == null ) {
            return null;
        }

        List<File> list = new ArrayList<File>( dtoList.size() );
        for ( FileDTO fileDTO : dtoList ) {
            list.add( toEntity( fileDTO ) );
        }

        return list;
    }
}
