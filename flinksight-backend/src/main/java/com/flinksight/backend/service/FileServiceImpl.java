package com.flinksight.backend.service;

import com.flinksight.backend.common.PageHelpers;
import com.flinksight.backend.domain.File;
import com.flinksight.backend.mapper.FileStructMapper;
import com.flinksight.backend.repository.FileRepository;
import com.flinksight.backend.security.SecurityUtil;
import com.flinksight.common.dto.FileDTO;
import com.flinksight.common.model.PageResult;
import com.flinksight.common.service.FileService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional
public class FileServiceImpl implements FileService {
    private final FileRepository repository;
    private final FileStructMapper fileStructMapper;

    @Override
    public FileDTO createOrUpdate(FileDTO fileDTO) {
        File entity = fileStructMapper.toEntity(fileDTO);
        entity.setIsDeleted(0);
        return fileStructMapper.toDTO(repository.save(entity));
    }

    @Override
    public Optional<FileDTO> getById(Long id) {
        return repository.findById(id).map(fileStructMapper::toDTO).filter(e -> e.getIsDeleted() == 0);
    }

    @Override
    public PageResult<FileDTO> list(int page, int size) {
        PageRequest pr = PageHelpers.pageRequest(page, size, null, File.class); // 统一 1→0
        Page<File> result = repository.findByTenantIdAndIsDeleted(SecurityUtil.getCurrentTenantId(),0, pr);
        return PageHelpers.toPageResult(result, fileStructMapper::toDTO, true); // 返回 1-ba
    }

    @Override
    public boolean sDelete(Long id) {
        Optional<FileDTO> opt = repository.findById(id).map(fileStructMapper::toDTO).filter(e -> e.getIsDeleted() == 0);
        if (opt.isPresent()) {
            FileDTO dto = opt.get();
            dto.setIsDeleted(1);
            repository.save(fileStructMapper.toEntity(dto));
            return true;
        }
        return false;
    }
}
