package com.flinksight.backend.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

/**
 * 通用软删除基类Repository，所有业务Repository都应继承此接口
 */
public interface SoftDeleteRepository<T, ID> extends JpaRepository<T, ID> {
    // 软删除
    default void softDeleteById(ID id) {
        T entity = findById(id).orElseThrow(() -> new IllegalArgumentException("数据不存在"));
        try {
            entity.getClass().getMethod("setIsDeleted", Integer.class).invoke(entity, 1);
            save(entity);
        } catch (Exception e) {
            throw new IllegalStateException("软删除失败", e);
        }
    }

    // 查询所有未删除
    default List<T> findAllNotDeleted() {
        return findAll().stream()
                .filter(e -> {
                    try {
                        Object val = e.getClass().getMethod("getIsDeleted").invoke(e);
                        return val == null || Integer.valueOf(val.toString()) == 0;
                    } catch (Exception ex) {
                        return true;
                    }
                })
                .toList();
    }
}