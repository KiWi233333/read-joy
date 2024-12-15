package com.readjoy.readjoyapi.common.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * Comment状态枚举类
 */
@Getter
@AllArgsConstructor
public enum CommentStatusEnum {
    // 评论的审核状态：1待审核，2已审核，3已驳回
    PENDING(1, "待审核"),
    APPROVED(2, "已审核"),
    REJECTED(3, "已驳回");

    private final Integer code;
    private final String description;


    public static CommentStatusEnum fromCode(Integer code) {
        for (CommentStatusEnum status : CommentStatusEnum.values()) {
            if (status.getCode().equals(code)) {
                return status;
            }
        }
        return null;
    }

}
