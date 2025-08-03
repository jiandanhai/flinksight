package com.flinksight.common.utils;

import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Date;

/**
 * 日期时间工具类（Java8+）
 * 支持毫秒/秒、格式化、时区转换等常用场景
 * 商业级可直接用
 */
public class DateUtil {
    public static final String DEFAULT_FORMAT = "yyyy-MM-dd HH:mm:ss";
    public static final ZoneId DEFAULT_ZONE = ZoneId.of("Asia/Shanghai");

    /** 获取当前时间戳（毫秒） */
    public static long nowMillis() {
        return System.currentTimeMillis();
    }

    /** 获取当前时间戳（秒） */
    public static long nowSeconds() {
        return Instant.now().getEpochSecond();
    }

    /** 获取当前本地时间 */
    public static LocalDateTime now() {
        return LocalDateTime.now(DEFAULT_ZONE);
    }

    /** 获取当前日期 */
    public static LocalDate today() {
        return LocalDate.now(DEFAULT_ZONE);
    }

    /** 获取当前时间，指定格式 */
    public static String nowStr(String pattern) {
        return now().format(DateTimeFormatter.ofPattern(pattern));
    }

    /** 获取当前时间，默认格式 */
    public static String nowStr() {
        return now().format(DateTimeFormatter.ofPattern(DEFAULT_FORMAT));
    }

    /** 时间戳（毫秒）转字符串 */
    public static String millisToStr(long millis, String pattern) {
        return LocalDateTime.ofInstant(Instant.ofEpochMilli(millis), DEFAULT_ZONE)
                .format(DateTimeFormatter.ofPattern(pattern));
    }

    /** 字符串转时间戳（毫秒） */
    public static long strToMillis(String dateStr, String pattern) {
        LocalDateTime dateTime = LocalDateTime.parse(dateStr, DateTimeFormatter.ofPattern(pattern));
        return dateTime.atZone(DEFAULT_ZONE).toInstant().toEpochMilli();
    }

    /** Date 转 LocalDateTime */
    public static LocalDateTime dateToLocal(Date date) {
        return LocalDateTime.ofInstant(date.toInstant(), DEFAULT_ZONE);
    }

    /** LocalDateTime 转 Date */
    public static Date localToDate(LocalDateTime dateTime) {
        return Date.from(dateTime.atZone(DEFAULT_ZONE).toInstant());
    }

    /** 时间戳（毫秒）转 LocalDateTime */
    public static LocalDateTime millisToLocal(long millis) {
        return LocalDateTime.ofInstant(Instant.ofEpochMilli(millis), DEFAULT_ZONE);
    }

    /** LocalDateTime 转时间戳（毫秒） */
    public static long localToMillis(LocalDateTime dateTime) {
        return dateTime.atZone(DEFAULT_ZONE).toInstant().toEpochMilli();
    }

    /** 指定时区格式化 */
    public static String formatWithZone(long millis, String pattern, String zoneId) {
        return LocalDateTime.ofInstant(Instant.ofEpochMilli(millis), ZoneId.of(zoneId))
                .format(DateTimeFormatter.ofPattern(pattern));
    }
}
