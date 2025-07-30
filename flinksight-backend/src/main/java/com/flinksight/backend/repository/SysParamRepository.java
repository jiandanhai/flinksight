package com.flinksight.backend.repository;

import com.flinksight.backend.domain.SysParam;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SysParamRepository extends JpaRepository<SysParam, Long> {
}
