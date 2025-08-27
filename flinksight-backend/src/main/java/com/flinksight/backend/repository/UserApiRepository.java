package com.flinksight.backend.repository;

import com.flinksight.backend.domain.UserApi;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserApiRepository extends JpaRepository<UserApi, Long> {
    Page<UserApi> findByUserIdAndIsDeleted(Long userId, Integer isDeleted, Pageable pageable);
    Page<UserApi> findByApiIdAndIsDeleted(Long apiId, Integer isDeleted, Pageable pageable);
    List<UserApi> findByUserIdAndApiIdAndIsDeleted(Long userId, Long apiId, Integer isDeleted);

    @Query("""
    SELECT ua FROM UserApi ua
     WHERE ua.isDeleted = 0
       AND (:userId IS NULL OR ua.userId = :userId)
       AND (:apiId  IS NULL OR ua.apiId  = :apiId)
  """)
    Page<UserApi> pageQuery(@Param("userId") Long userId,
                            @Param("apiId")  Long apiId,
                            Pageable pageable);
}
