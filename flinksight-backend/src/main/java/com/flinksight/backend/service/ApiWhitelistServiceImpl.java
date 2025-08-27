package com.flinksight.backend.service;

import com.flinksight.backend.domain.ApiWhitelist;
import com.flinksight.backend.mapper.ApiWhitelistStructMapper;
import com.flinksight.backend.repository.ApiWhitelistRepository;
import com.flinksight.common.dto.ApiWhitelistDTO;
import com.flinksight.common.service.ApiWhitelistService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional
public class ApiWhitelistServiceImpl implements ApiWhitelistService {
    private final ApiWhitelistRepository repository;
    private final ApiWhitelistStructMapper apiWhitelistStructMapper;

    @Override
    public ApiWhitelistDTO createOrUpdate(ApiWhitelistDTO apiWhitelistDTO) {
        ApiWhitelist entity = apiWhitelistStructMapper.toEntity(apiWhitelistDTO);
        ApiWhitelist saved = repository.save(entity);
        entity.setIsDeleted(0);
        return apiWhitelistStructMapper.toDTO(saved);
    }

    @Override
    public Optional<ApiWhitelistDTO> getById(Long id) {
        return repository.findById(id).map(apiWhitelistStructMapper::toDTO).filter(e -> e.getIsDeleted() == 0);
    }

    @Override
    public boolean sDelete(Long id) {
        Optional<ApiWhitelistDTO> opt = repository.findById(id).map(apiWhitelistStructMapper::toDTO).filter(e -> e.getIsDeleted() == 0);
        if (opt.isPresent()) {
            ApiWhitelistDTO dto = opt.get();
            dto.setIsDeleted(1);
            ApiWhitelist entity = apiWhitelistStructMapper.toEntity(dto);
            repository.save(entity);
            return true;
        }
        return false;
    }
}
