package com.flinksight.backend.repository;

import com.flinksight.backend.domain.SysParam;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SysParamRepository extends JpaRepository<SysParam, Long> {
    Page<SysParam> findByIsDeleted(Integer isDeleted, Pageable pageable);
}
