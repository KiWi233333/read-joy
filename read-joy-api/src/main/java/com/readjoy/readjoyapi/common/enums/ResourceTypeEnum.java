package com.readjoy.readjoyapi.common.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * Resource type enum.
 */
@Getter
@AllArgsConstructor
public enum ResourceTypeEnum {
    // 1 - 文件，2 - 图片， 3 - 视频， 4 - 音频 5 - 压缩包 6 - 链接 7 - 其他
    FILE(1, "文件"),
    IMAGE(2, "图片"),
    VIDEO(3, "视频"),
    AUDIO(4, "音频"),
    ZIP(5, "压缩包"),
    LINK(6, "链接"),
    OTHER(7, "其他");

    private final Integer code;
    private final String value;

    public static final Integer MAX_CODE = 7;
    public static final String MAX_VALUE = "其他";
    public static final Integer MIN_CODE = 1;
    public static final String MIN_VALUE = "文件";

    public static ResourceTypeEnum fromCode(Integer code) {
        for (ResourceTypeEnum resourceTypeEnum : ResourceTypeEnum.values()) {
            if (resourceTypeEnum.getCode().equals(code)) {
                return resourceTypeEnum;
            }
        }
        return null;
    }

}
