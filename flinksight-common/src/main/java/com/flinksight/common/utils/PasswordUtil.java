package com.flinksight.common.utils;

import org.springframework.security.crypto.bcrypt.BCrypt;

public class PasswordUtil {
    // 生成加密密码
    public static String encode(String rawPwd) {
        return BCrypt.hashpw(rawPwd, BCrypt.gensalt());
    }

    // 密码校验
    public static boolean matches(String rawPwd, String hashedPwd) {
        return BCrypt.checkpw(rawPwd, hashedPwd);
    }
}
