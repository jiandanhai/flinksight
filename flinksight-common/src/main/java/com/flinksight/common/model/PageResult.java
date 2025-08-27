// src/main/java/com/flinksight/common/model/PageResult.java
package com.flinksight.common.model;

import lombok.Data;
import org.springframework.data.domain.Page;

import java.util.List;

@Data
public class PageResult<T> {
    private long total;
    private int page;      // 建议统一 0-based；如果前端要 1-based，返回时 +1
    private int size;
    private int pages;
    private List<T> data;

    /** 用 Spring Data Page 直接构造 */
    public PageResult(Page<T> pageData) {
        this.total = pageData.getTotalElements();
        this.page  = pageData.getNumber();
        this.size  = pageData.getSize();
        this.data  = pageData.getContent();
        this.pages = size > 0 ? (int) Math.ceil((double) total / (double) size) : 1;
    }

    /** 用明细字段构造 */
    public PageResult(List<T> data, long total, int page, int size) {
        this.data  = data;
        this.total = total;
        this.page  = page;
        this.size  = size;
        this.pages = size > 0 ? (int) Math.ceil((double) total / (double) size) : 1;
    }

    /** 工厂：从 Page<T> 生成 */
    public static <T> PageResult<T> from(Page<T> page) {
        return new PageResult<>(page);
    }

    /** 工厂：从自定义列表 + 统计值生成 */
    public static <T> PageResult<T> of(List<T> records, long total, int page, int size) {
        return new PageResult<>(records, total, page, size);
    }
}
