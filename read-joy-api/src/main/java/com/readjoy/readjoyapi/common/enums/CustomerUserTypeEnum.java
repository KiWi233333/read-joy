package com.readjoy.readjoyapi.common.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 用户类型
 */
@Getter
@AllArgsConstructor
public enum CustomerUserTypeEnum {
    NORMAL(1, "普通用户"),
    ;
    private final int type;
    private final String label;
}
