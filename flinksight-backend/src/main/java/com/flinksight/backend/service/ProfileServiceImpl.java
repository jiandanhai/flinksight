package com.flinksight.backend.service;

import com.flinksight.backend.domain.Permission;
import com.flinksight.backend.domain.Profile;
import com.flinksight.backend.mapper.PermissionStructMapper;
import com.flinksight.backend.mapper.ProfileStructMapper;
import com.flinksight.backend.repository.ProfileRepository;
import com.flinksight.common.dto.PermissionDTO;
import com.flinksight.common.dto.ProfileDTO;
import com.flinksight.common.service.ProfileService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional
public class ProfileServiceImpl implements ProfileService {
    private final ProfileRepository repository;
    private final ProfileStructMapper mapper;

    @Override
    public ProfileDTO createOrUpdate(ProfileDTO profileDTO) {
        Profile entity = mapper.toEntity(profileDTO);
        entity.setIsDeleted(0);
        Profile saved = repository.save(entity);
        return mapper.toDTO(saved);
    }

    @Override
    public Optional<ProfileDTO> getById(Long id) {
        return repository.findById(id).map(mapper::toDTO).filter(e -> e.getIsDeleted() == 0);
    }

    @Override
    public ProfileDTO getByUserId(Long userId) {
        return mapper.toDTO(repository.findByUserIdAndIsDeleted(userId, 0));
    }

    @Override
    public boolean softDelete(Long id) {
        Optional<ProfileDTO> opt = repository.findById(id).map(mapper::toDTO).filter(e -> e.getIsDeleted() == 0);
        if (opt.isPresent()) {
            ProfileDTO dto = opt.get();
            dto.setIsDeleted(1);
            repository.save(mapper.toEntity(dto));
            return true;
        }
        return false;
    }
}
