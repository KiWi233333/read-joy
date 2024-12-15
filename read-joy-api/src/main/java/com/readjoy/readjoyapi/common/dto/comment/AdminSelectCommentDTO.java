package com.readjoy.readjoyapi.common.dto.comment;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.readjoy.readjoyapi.common.dto.defaults.PageDTO;
import com.readjoy.readjoyapi.common.enums.BoolEnum;
import com.readjoy.readjoyapi.common.enums.CommentStatusEnum;
import com.readjoy.readjoyapi.common.vo.comment.CommentVO;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.Range;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;


@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class AdminSelectCommentDTO extends PageDTO<CommentVO> {

    @Length(max = 100, message = "关键字不超过100字！")
    @Schema(description = "关键字", example = "真好")
    private String keyword;

    /**
     * 评论的审核状态：1待审核，2已审核，3已驳回
     *
     * @see BoolEnum
     */
    @Schema(description = "评论的审核状态：1待审核，2已审核，3已驳回", example = "1")
    @Range(min = 1, max = 3, message = "评论的审核状态错误！")
    private Integer status;

    @Schema(description = "是否删除 0-未删除 1-已删除", example = "1")
    @Range(min = 0, max = 1, message = "是否删除参数字段错误！")
    private Integer isDeleted;

    @Schema(description = "书籍ID", example = "101")
    private Integer bookId;

    @Schema(description = "用户ID", example = "1001")
    private Integer commentator;

    @Schema(description = "创建时间排序 1-降序 desc 2-升序 asc")
    @Range(min = 1, max = 2, message = "升序降序参数错误！")
    private Integer sortOrder = 1;

    @Schema(description = "开始时间(精度：天)", example = "2024-12-14 00:00:00")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date startDateTime;

    @Schema(description = "结束时间(精度：天)", example = "2024-12-14 00:00:00")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private String endDateTime;

    /**
     * 客户端公共参数
     */
    public static AdminSelectCommentDTO toViewDTO(SelectCommentDTO dto, Integer bookId, Integer userId) {
        AdminSelectCommentDTO comDTO = new AdminSelectCommentDTO()
                .setIsDeleted(BoolEnum.FALSE.getValue()) // 默认不显示已删除评论
                .setBookId(bookId)
                .setStatus(CommentStatusEnum.APPROVED.getCode()) // 已审核
                .setSortOrder(dto.getSortOrder())
                .setStartDateTime(dto.getStartDateTime())
                .setEndDateTime(dto.getEndDateTime())
                .setCommentator(dto.isOnlyMine() ? userId : null);
        comDTO.setPage(dto.getPage());
        comDTO.setSize(dto.getSize());
        return comDTO;
    }

    /**
     * 客户端公共参数
     */
    public static AdminSelectCommentDTO toUserDTO(SelectCommentDTO dto, Integer userId) {
        AdminSelectCommentDTO comDTO = new AdminSelectCommentDTO()
                .setIsDeleted(BoolEnum.FALSE.getValue()) // 默认不显示已删除评论
                .setCommentator(userId)
                .setStatus(CommentStatusEnum.APPROVED.getCode()) // 已审核
                .setSortOrder(dto.getSortOrder())
                .setStartDateTime(dto.getStartDateTime())
                .setEndDateTime(dto.getEndDateTime())
                .setCommentator(dto.getOnlyMine() == 1 ? userId : null);
        comDTO.setPage(dto.getPage());
        comDTO.setSize(dto.getSize());
        return comDTO;
    }

    public boolean checkDesc() {
        return sortOrder != null && sortOrder == 1;
    }

    public boolean checkAsc() {
        return sortOrder != null && sortOrder == 2;
    }

    public boolean checkTimeSort() {
        return startDateTime != null && endDateTime != null;
    }
}
