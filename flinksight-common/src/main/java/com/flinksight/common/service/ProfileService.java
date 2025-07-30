package com.flinksight.common.service;

import com.flinksight.common.dto.ProfileDTO;

import java.util.Optional;

public interface ProfileService extends SoftDeleteService<ProfileDTO, Long> {
    ProfileDTO createOrUpdate(ProfileDTO entity);
    Optional<ProfileDTO> getById(Long id);
    ProfileDTO getByUserId(Long userId);
}
