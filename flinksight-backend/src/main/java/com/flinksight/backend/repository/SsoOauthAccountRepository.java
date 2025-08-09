package com.flinksight.backend.repository;


import com.flinksight.backend.domain.OauthAccount;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

/**
 * 第三方授权账号 仓库
 */
@Repository
public interface SsoOauthAccountRepository extends JpaRepository<OauthAccount, Long> {
    Page<OauthAccount> findByUserIdAndIsDeleted(Long userId, Integer isDeleted, Pageable pageable);

    Optional<OauthAccount> findByProviderAndOpenidAndIsDeleted(String provider, String openid, Integer isDeleted);

    boolean existsByUserIdAndProviderAndIsDeleted(Long userId, String provider, Integer isDeleted);

    /**
     * 解绑指定用户的某个平台三方账户（直接物理删除）
     */
    @Transactional
    @Modifying
    @Query("DELETE FROM OauthAccount o WHERE o.userId = :userId AND o.provider = :provider")
    int deleteByUserIdAndProvider(Long userId, String provider);

    // 如果你是软删除（isDeleted），可以用update
    @Transactional
    @Modifying
    @Query("UPDATE OauthAccount o SET o.isDeleted = 1 WHERE o.userId = :userId AND o.provider = :provider AND o.isDeleted = 0")
    int markUnbindByUserIdAndProvider(Long userId, String provider);
}
