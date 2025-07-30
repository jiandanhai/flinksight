package com.flinksight.backend.repository;

import com.flinksight.backend.domain.ApiWhitelist;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ApiWhitelistRepository extends JpaRepository<ApiWhitelist, Long> {
}
