package com.flinksight.backend.repository;

import com.flinksight.backend.domain.Dict;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface DictRepository extends JpaRepository<Dict, Long> {
    List<Dict> findByDictTypeAndIsDeleted(String dictType, Integer isDeleted);
}
