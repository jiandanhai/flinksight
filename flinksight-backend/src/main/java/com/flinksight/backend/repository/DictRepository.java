package com.flinksight.backend.repository;

import com.flinksight.backend.domain.Dict;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DictRepository extends JpaRepository<Dict, Long> {
    Page<Dict> findByDictTypeAndIsDeleted(String dictType, Integer isDeleted, Pageable pageable);
}
