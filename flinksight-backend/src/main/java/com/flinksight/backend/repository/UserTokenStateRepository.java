package com.flinksight.backend.repository;

import com.flinksight.backend.domain.UserTokenState;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserTokenStateRepository extends JpaRepository<UserTokenState, Long> {
}