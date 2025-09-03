package com.flinksight.common.utils;

import java.time.Instant;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;


public class TimeUtil {
private static final DateTimeFormatter ISO = DateTimeFormatter.ISO_OFFSET_DATE_TIME;
public static String nowIso(){ return ISO.format(Instant.now().atOffset(ZoneOffset.UTC)); }
}