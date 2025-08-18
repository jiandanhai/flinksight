package com.flinksight.backend.mapper;


import com.flinksight.backend.domain.Menu;
import com.flinksight.common.dto.MenuNodeDTO;
import org.mapstruct.Context;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.springframework.context.i18n.LocaleContextHolder;

import java.util.Locale;

@Mapper(componentModel = "spring", config = BaseStructMapperConfig.class)
public interface MenuStructMapper extends GenericMapper<MenuNodeDTO, Menu> {

    // 给 MapStruct 生成实现用的主方法（带 @Context）
    @Mapping(target = "id",       source = "id")
    @Mapping(target = "key",      source = "menuKey")
    @Mapping(target = "path",     source = "path")
    @Mapping(target = "icon",     source = "icon")
    @Mapping(target = "orderNum", source = "orderNum")
    // title 走表达式，由下面的辅助方法挑选
    @Mapping(target = "title", expression = "java( pickTitle(menu, locale) )")
    MenuNodeDTO toDTO(Menu menu, @Context Locale locale);

    // 覆盖父接口：无 Locale 的版本，使用当前线程的 Locale
    @Override
    default MenuNodeDTO toDTO(Menu menu) {
        return toDTO(menu, LocaleContextHolder.getLocale());
    }

    // MapStruct 可调用的辅助方法
    default String pickTitle(Menu m, Locale locale) {
        String lang = (locale != null ? locale.getLanguage() : "zh");
        String title = "en".equalsIgnoreCase(lang) ? m.getTitleEn() : m.getTitleZh();
        return (title != null && !title.isBlank()) ? title : m.getMenuKey();
    }
}