package com.readjoy.readjoyapi.common.dto.comment;

import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.github.houbb.sensitive.word.core.SensitiveWordHelper;
import com.readjoy.readjoyapi.common.enums.BoolEnum;
import com.readjoy.readjoyapi.common.enums.CommentStatusEnum;
import com.readjoy.readjoyapi.common.pojo.Comment;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;
import org.hibernate.validator.constraints.Length;

import java.io.Serializable;

/**
 * 评论信息 DTO
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Accessors(chain = true)
@Schema(description = "评论信息")
public class InsertCommentDTO implements Serializable {

    @Schema(description = "书籍ID", example = "1")
    @NotNull(message = "评论书籍不能为空！")
    private Integer bookId;

    @Schema(description = "评论内容")
    @NotNull(message = "评论内容不能为空！")
    @Length(min = 1, message = "评论内容长度不能少于1个字符！")
    @Length(max = 500, message = "评论内容长度不能超过500！")
    private String commentBody;

    public Comment toEntity(Integer userId, boolean isFiltered) {
        // 过滤敏感词
        String content = this.getCommentBody();
        if (isFiltered && StringUtils.isNotBlank(content)) {
            content = SensitiveWordHelper.replace(this.getCommentBody());
        }
        return new Comment()
                .setBookId(this.getBookId())
                .setCommentBody(content)
                .setCommentator(userId)
                .setCommentStatus(isFiltered ? CommentStatusEnum.APPROVED.getCode() : CommentStatusEnum.PENDING.getCode()) // 过滤后直接通过
                .setIsDeleted(BoolEnum.FALSE.getValue());// 默认不删除
    }
}
