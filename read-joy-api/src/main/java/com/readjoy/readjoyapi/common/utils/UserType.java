package com.readjoy.readjoyapi.common.utils;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 用户类型
 */
@Getter
@AllArgsConstructor
public enum UserType {
    CUSTOMER(0, "用户"),
    ADMIN(1, "管理员"),
    ;
    private final int type;
    private final String label;
}
