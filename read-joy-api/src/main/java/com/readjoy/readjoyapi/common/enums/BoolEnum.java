package com.readjoy.readjoyapi.common.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * Boolean枚举类
 */
@Getter
@AllArgsConstructor
public enum BoolEnum {
    TRUE(1, "true"),
    FALSE(0, "false");
    private final Integer value;
    private final String description;
}
