package com.readjoy.readjoyapi.common.dto.resource;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.readjoy.readjoyapi.common.dto.defaults.PageDTO;
import com.readjoy.readjoyapi.common.vo.resource.AdminResourceVO;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.Range;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;


@EqualsAndHashCode(callSuper = true)
@Data
@Accessors(chain = true)
public class SelectResourceDTO extends PageDTO<AdminResourceVO> {

    @Length(max = 100, message = "关键字不超过100字！")
    @Schema(description = "关键字", example = "java")
    private String keyword;

    @Schema(description = "分类id", example = "1")
    private Integer bookId;

    @Schema(description = "排序方式 1-按发布时间 2-按下载量 3-按点赞")
    @Range(min = 1, max = 3, message = "排序方式只能为1或2！")
    private Integer sortType;

    @Schema(description = "排序方式 1-降序 desc 2-升序 asc")
    @Range(min = 1, max = 2, message = "升序降序只能为1或2！")
    private Integer sortOrder;

    @Schema(description = "开始时间(精度：天)", example = "2021-01-01")
    @JsonFormat(pattern = "yyyy-MM-dd")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date startDate;

    @Schema(description = "结束时间(精度：天)", example = "2021-12-31")
    @JsonFormat(pattern = "yyyy-MM-dd")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private String endDate;


    public boolean checkIsSortByPubDate() {
        return sortType != null && sortType == 1;
    }

    public boolean checkIsSortByDownloadCount() {
        return sortType != null && sortType == 2;
    }
    public boolean checkIsSortByLikeCount() {
        return sortType != null && sortType == 3;
    }

    public boolean checkDesc() {
        return sortOrder != null && sortOrder == 1;
    }

    public boolean checkAsc() {
        return sortOrder != null && sortOrder == 2;
    }
}
