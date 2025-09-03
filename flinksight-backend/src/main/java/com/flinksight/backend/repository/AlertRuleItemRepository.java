package com.flinksight.backend.repository;

import com.flinksight.backend.domain.AlertRuleItem;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface AlertRuleItemRepository extends JpaRepository<AlertRuleItem, Long> {
  List<AlertRuleItem> findByRuleSetId(Long ruleSetId);
  void deleteByRuleSetId(Long ruleSetId);

  Page<AlertRuleItem> findByRuleSetId(Long ruleSetId, Pageable pageable);

  Page<AlertRuleItem> findByRuleSetIdAndEnabled(Long ruleSetId, Integer enabled, Pageable pageable);

  Optional<AlertRuleItem> findByIdAndRuleSetId(Long id, Long ruleSetId);

  /* 软删除：标记 is_deleted=1 并打 deleted_at */
  @Modifying
  @Query("update AlertRuleItem a set a.isDeleted=1, a.deletedAt=CURRENT_TIMESTAMP " +
          "where a.ruleSetId=:ruleSetId and a.id in :ids and a.isDeleted=0")
  int softDeleteByRuleSetIdAndIdIn(@Param("ruleSetId") Long ruleSetId, @Param("ids") List<Long> ids);

  /* 批量启用/禁用（只作用于未删除记录） */
  @Modifying
  @Query("update AlertRuleItem a set a.enabled=:enabled " +
          "where a.ruleSetId=:ruleSetId and a.id in :ids and a.isDeleted=0")
  int updateEnabledByIds(@Param("ruleSetId") Long ruleSetId, @Param("ids") List<Long> ids, @Param("enabled") Integer enabled);

  long countByRuleSetId(Long ruleSetId); // 用于列表聚合统计
}
