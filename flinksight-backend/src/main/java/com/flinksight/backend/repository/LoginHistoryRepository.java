package com.flinksight.backend.repository;

import com.flinksight.backend.domain.LoginHistory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface LoginHistoryRepository extends JpaRepository<LoginHistory, Long> {
    List<LoginHistory> findByUserIdAndIsDeleted(Long userId, Integer isDeleted);
}
