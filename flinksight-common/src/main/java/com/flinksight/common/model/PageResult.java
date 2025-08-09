// src/main/java/com/flinksight/common/model/PageResult.java
package com.flinksight.common.model;

import lombok.Data;
import org.springframework.data.domain.Page;

import java.util.List;

@Data
public class PageResult<T> {
    private long total;
    private int page;
    private int size;
    private List<T> data;

    public PageResult(Page<T> pageData) {
        this.total = pageData.getTotalElements();
        this.page = pageData.getNumber();
        this.size = pageData.getSize();
        this.data = pageData.getContent();
    }
    // 增加此构造方法！
    public PageResult(List<T> data, long total, int page, int size) {
        this.data = data;
        this.total = total;
        this.page = page;
        this.size = size;
    }
}
