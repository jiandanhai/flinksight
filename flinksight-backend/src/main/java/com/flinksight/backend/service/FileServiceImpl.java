package com.flinksight.backend.service;

import com.flinksight.backend.domain.DataSource;
import com.flinksight.backend.domain.File;
import com.flinksight.backend.mapper.DataSourceStructMapper;
import com.flinksight.backend.mapper.FileStructMapper;
import com.flinksight.backend.repository.FileRepository;
import com.flinksight.common.dto.ApiKeyDTO;
import com.flinksight.common.dto.DataSourceDTO;
import com.flinksight.common.dto.FileDTO;
import com.flinksight.common.service.FileService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional
public class FileServiceImpl implements FileService {
    private final FileRepository repository;
    private final FileStructMapper mapper;

    @Override
    public FileDTO createOrUpdate(FileDTO fileDTO) {
        File entity = mapper.toEntity(fileDTO);
        entity.setIsDeleted(0);
        return mapper.toDTO(repository.save(entity));
    }

    @Override
    public Optional<FileDTO> getById(Long id) {
        return repository.findById(id).map(mapper::toDTO).filter(e -> e.getIsDeleted() == 0);
    }

    @Override
    public List<FileDTO> findByTenantId(Long tenantId) {
        return mapper.toDTOList(repository.findByTenantIdAndIsDeleted(tenantId,0));
    }

    @Override
    public List<FileDTO> getAll() {
        return mapper.toDTOList(repository.findAll().stream().filter(e -> e.getIsDeleted() == 0).toList());
    }

    @Override
    public boolean softDelete(Long id) {
        Optional<FileDTO> opt = repository.findById(id).map(mapper::toDTO).filter(e -> e.getIsDeleted() == 0);
        if (opt.isPresent()) {
            FileDTO dto = opt.get();
            dto.setIsDeleted(1);
            repository.save(mapper.toEntity(dto));
            return true;
        }
        return false;
    }
}
