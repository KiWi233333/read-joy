package com.readjoy.readjoyapi.common.dto.comment;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.readjoy.readjoyapi.common.dto.defaults.PageDTO;
import com.readjoy.readjoyapi.common.vo.comment.CommentVO;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import org.hibernate.validator.constraints.Range;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;


@EqualsAndHashCode(callSuper = true)
@Data
@Accessors(chain = true)
public class SelectCommentDTO extends PageDTO<CommentVO> {

    @Schema(description = "创建时间排序 1-降序 desc 2-升序 asc")
    @Range(min = 1, max = 2, message = "升序降序只能为1或2！")
    private Integer sortOrder = 1;

    @Schema(description = "只看自己")
    @Range(min = 0, max = 1, message = "请求参数错误，请重新输入！")
    private Integer onlyMine;

    @Schema(description = "开始时间", example = "2021-01-01")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date startDateTime;

    @Schema(description = "结束时间", example = "2021-12-31")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private String endDateTime;

    public boolean isOnlyMine() {
        return onlyMine != null && onlyMine == 1;
    }

    public boolean checkDesc() {
        return sortOrder != null && sortOrder == 1;
    }

    public boolean checkAsc() {
        return sortOrder != null && sortOrder == 2;
    }
}
