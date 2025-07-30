package com.flinksight.common.utils;

import java.util.UUID;

public class IdGenUtil {
    public static String genUuid() {
        return UUID.randomUUID().toString().replace("-", "");
    }

    public static String genShortUuid() {
        return UUID.randomUUID().toString().split("-")[0];
    }
}
