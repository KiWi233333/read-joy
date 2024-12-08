package com.readjoy.readjoyapi.common.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 用户类型
 */
@Getter
@AllArgsConstructor
public enum SysUserTypeEnum {
    CUSTOMER(0, "用户"),
    ADMIN(1, "管理员"),
    ;
    private final int type;
    private final String label;

    public static boolean isAdmin(Integer userType) {
        return userType != null && userType == SysUserTypeEnum.ADMIN.getType();
    }

    public static boolean isUser(Integer userType) {
        return userType != null && userType == SysUserTypeEnum.CUSTOMER.getType();
    }
}
