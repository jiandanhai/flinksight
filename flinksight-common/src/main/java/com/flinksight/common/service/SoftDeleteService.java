package com.flinksight.common.service;

/**
 * 通用软删除接口（工具类/基类Service）
 * @param <T>
 * @param <ID>
 */
public interface SoftDeleteService<T, ID> {
    boolean softDelete(ID id);
}