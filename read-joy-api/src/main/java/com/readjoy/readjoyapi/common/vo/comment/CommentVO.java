package com.readjoy.readjoyapi.common.vo.comment;

import com.baomidou.mybatisplus.annotation.*;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.readjoy.readjoyapi.common.enums.CommentStatusEnum;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.io.Serial;
import java.io.Serializable;
import java.util.Date;

/**
 * 评论VO对象
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Accessors(chain = true)
public class CommentVO implements Serializable {

    /**
     * 评论ID
     */
    @Schema(description = "评论ID", example = "1")
    private Integer id;

    /**
     * 书籍ID
     */
    @Schema(description = "书籍ID", example = "101")
    private Integer bookId;

    /**
     * 评论者ID
     */
    @Schema(description = "评论者ID", example = "1001")
    private Integer commentator;

    @Schema(description = "用户昵称", example = "张三")
    private String commentatorName; // username

    @Schema(description = "用户头像", example = "files/avatar.jpg")
    private String commentatorAvatar;

    /**
     * 评论内容
     */
    @Schema(description = "评论内容")
    private String commentBody;

    /**
     * 评论状态：1待审核 2已审核 3已驳回
     */
    @Schema(description = "审核状态：1待审核，2已审核，3已驳回", example = "1")
    private Integer commentStatus;

    @Schema(description = "审核状态文本", example = "待审核")
    private String commentStatusText;

    // 设置commentStatusText不要删除
    public void setCommentStatus(Integer commentStatus) {
        final CommentStatusEnum statusEnum = CommentStatusEnum.fromCode(commentStatus);
        if (statusEnum != null) {
            this.commentStatusText = statusEnum.getDescription();
        } else {
            this.commentStatusText = "";
        }
        this.commentStatus = commentStatus;
    }

    /**
     * 是否删除
     */
    @Schema(description = "是否已删除：0为否，1为是", example = "0")
    private Integer isDeleted;

    /**
     * 创建时间
     */
    @Schema(description = "创建时间", example = "2023-01-01 12:00:00")
    @TableField(fill = FieldFill.INSERT)
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date createTime;

    @Serial
    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}
