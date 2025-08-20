package com.flinksight.backend.menu;

import java.util.List;

/**
 *POJO，避免依赖 Lombok
 */
public class MenuMap {
    private List<Top> tops;

    public List<Top> getTops() { return tops; }
    public void setTops(List<Top> tops) { this.tops = tops; }

    public static class Top {
        private String code;
        private String titleZh;
        private String titleEn;
        private String path;
        private String icon;
        private Integer order;
        private String perm;
        private List<Item> children;

        public String getCode() { return code; }
        public void setCode(String code) { this.code = code; }
        public String getTitleZh() { return titleZh; }
        public void setTitleZh(String titleZh) { this.titleZh = titleZh; }
        public String getTitleEn() { return titleEn; }
        public void setTitleEn(String titleEn) { this.titleEn = titleEn; }
        public String getPath() { return path; }
        public void setPath(String path) { this.path = path; }
        public String getIcon() { return icon; }
        public void setIcon(String icon) { this.icon = icon; }
        public Integer getOrder() { return order; }
        public void setOrder(Integer order) { this.order = order; }
        public String getPerm() { return perm; }
        public void setPerm(String perm) { this.perm = perm; }
        public List<Item> getChildren() { return children; }
        public void setChildren(List<Item> children) { this.children = children; }
    }

    public static class Item {
        private String code;
        private String titleZh;
        private String titleEn;
        private String path;
        private Integer order;
        private String perm;

        public String getCode() { return code; }
        public void setCode(String code) { this.code = code; }
        public String getTitleZh() { return titleZh; }
        public void setTitleZh(String titleZh) { this.titleZh = titleZh; }
        public String getTitleEn() { return titleEn; }
        public void setTitleEn(String titleEn) { this.titleEn = titleEn; }
        public String getPath() { return path; }
        public void setPath(String path) { this.path = path; }
        public Integer getOrder() { return order; }
        public void setOrder(Integer order) { this.order = order; }
        public String getPerm() { return perm; }
        public void setPerm(String perm) { this.perm = perm; }
    }
}
