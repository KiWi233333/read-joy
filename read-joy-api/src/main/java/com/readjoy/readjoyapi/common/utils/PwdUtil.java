package com.readjoy.readjoyapi.common.utils;


import cn.hutool.crypto.digest.BCrypt;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

/**
 * 密码加密解密工具类
 */
@Component
@PropertySource(value = "classpath:application.properties")
public class PwdUtil {

    @Value("${bcrypt.salt}")
    private String slat = "$2a$10$rCJA1lRiGtniek27XQujbe";

    public String encrypt(String password) {
        return BCrypt.hashpw(password, slat);
    }

    public boolean check(String password, String checkPassword) {
        return BCrypt.checkpw(password, encrypt(checkPassword));
    }
}