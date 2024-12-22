package com.readjoy.readjoyapi.common.vo.other;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.readjoy.readjoyapi.common.vo.resource.AdminResourceVO;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;
import java.util.List;

/**
 * 首页统计数据
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Accessors(chain = true)
public class AdminHomeStatisticsVO {

    @Schema(description = "图书总量")
    private Long bookCount;
    @Schema(description = "图书类别总量")
    private Long bookCategoryCount;
    @Schema(description = "用户总量")
    private Long userCount;
    @Schema(description = "总下载量")
    private Long totalDownloadCount;
    @Schema(description = "资源总数")
    private Long resourceCount;
    @Schema(description = "总评论数")
    private Long totalCommentCount;
    @Schema(description = "待审核评论数")
    private Long pendingCommentCount;
    @Schema(description = "已驳回评论数")
    private Long rejectionCommentCount;

    @Schema(description = "按天统计发表图书统计")
    private List<BookPublishTotalByDay> bookPublishTotalByDayList;
    @Schema(description = "按月统计发表图书统计")
    private List<BookPublishTotalByMonth> bookPublishTotalByMonthList;
    @Schema(description = "按年统计发表图书统计")
    private List<BookPublishTotalByYear> bookPublishTotalByYearList;

    @Schema(description = "下载量排行榜(前20)")
    private List<AdminResourceVO> downloadCountRankingList;

    @Schema(description = "按类别图书总量统计")
    private List<BookCategoryTotal> bookCategoryTotalList;

    @Schema(description = "请求统计时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date requestTime;


}
