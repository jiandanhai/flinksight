package com.flinksight.backend.repository;

import com.flinksight.backend.domain.Profile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProfileRepository extends JpaRepository<Profile, Long> {
    Profile findByUserIdAndIsDeleted(Long userId, Integer isDeleted);
}
