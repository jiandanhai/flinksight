package com.flinksight.backend.repository;

import com.flinksight.backend.domain.UserApi;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface UserApiRepository extends JpaRepository<UserApi, Long> {
    List<UserApi> findByUserIdAndIsDeleted(Long userId, Integer isDeleted);
    List<UserApi> findByApiIdAndIsDeleted(Long apiId, Integer isDeleted);
}
