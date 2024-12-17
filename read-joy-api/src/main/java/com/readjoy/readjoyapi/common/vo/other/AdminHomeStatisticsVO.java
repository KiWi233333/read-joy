package com.readjoy.readjoyapi.common.vo.other;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.util.List;

/**
 * 首页统计数据
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Accessors(chain = true)
public class AdminHomeStatisticsVO {

    //    首页数据统计 图书总量、用户数量、总下载量、资源总数、总评论数
    @Schema(description = "图书总量")
    private Long bookCount;
    @Schema(description = "用户数量")
    private Long userCount;
    @Schema(description = "总下载量")
    private Long totalDownloadCount;
    @Schema(description = "资源总数")
    private Long resourceCount;
    @Schema(description = "总评论数")
    private Long totalCommentCount;

    @Schema(description = "按天统计发表图书统计")
    private List<BookPublishTotalByDay> bookPublishTotalByDayList;
    @Schema(description = "按月统计发表图书统计")
    private List<BookPublishTotalByMonth> bookPublishTotalByMonthList;

    @Schema(description = "按类别图书总量统计")
    private List<BookCategoryTotal> bookCategoryTotalList;


}
