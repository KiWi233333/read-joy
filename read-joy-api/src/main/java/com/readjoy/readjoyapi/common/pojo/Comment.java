package com.readjoy.readjoyapi.common.pojo;

import com.baomidou.mybatisplus.annotation.*;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;
import java.util.Date;

/**
 * 评论表
 * @TableName comment
 */
@TableName(value ="comment")
@Data
public class Comment implements Serializable {
    /**
     * 评论ID
     */
    @TableId(type = IdType.AUTO)
    private Integer id;

    /**
     * 书籍ID
     */
    private Integer bookId;

    /**
     * 评论者ID
     */
    private Integer commentator;

    /**
     * 评论内容
     */
    private String commentBody;

    /**
     * 评论状态：1待审核 2已审核 3已驳回
     */
    private Integer commentStatus;

    /**
     * 是否删除
     */
    private Integer isDeleted;

    @Schema(description = "创建时间", example = "2023-01-01T12:00:00Z")
    @TableField(fill = FieldFill.INSERT)
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date createTime;

    @Serial
    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}