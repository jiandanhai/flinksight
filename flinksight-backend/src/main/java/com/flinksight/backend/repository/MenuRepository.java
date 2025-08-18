package com.flinksight.backend.repository;

import com.flinksight.backend.domain.Menu;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface MenuRepository extends JpaRepository<Menu, Long> {
  /**
   * 查询全部“有效”(未删除)菜单，并按 orderNum 升序、再按 id 升序。
   * - 这里用 COALESCE 将 orderNum 为 null 的放到最后，避免前端排序错位。
   * - 如果你的实体字段不是 isDeleted，请把字段名改成你实体里的对应属性名。
   */
  @Query("""
        select m
          from Menu m
         where m.isDeleted = 0
         order by coalesce(m.orderNum, 2147483647), m.id
    """)
  List<Menu> findAllActiveOrderByOrderNumAsc();

}