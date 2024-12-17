import type { BoolEnum, Result } from "../types/result";
import { useHttp } from "../../utils/useHttp";


/**
 * 获取首页统计数据
 * @param dto
 * @returns Result<SelectHomeStatisticsDto>
 */
export function selectHomeStatistics(dto: SelectHomeStatisticsDto) {
  return useHttp.get<Result<SelectHomeStatisticsDto>>("/admin/res/statistics/home", dto);
}

export interface SelectHomeStatisticsDto {
  /**
   * 结束时间
   */
  endDateTime?: string;
  /**
   * 开始时间
   */
  startDateTime?: string;

}

/**
 * 返回数据
 *
 * AdminHomeStatisticsVO
 */
export interface AdminHomeStatisticsVO {
  /**
   * 按类别图书总量统计
   */
  bookCategoryTotalList?: BookCategoryTotal[];
  /**
   * 首页数据统计 图书总量、用户数量、总下载量、资源总数、总评论数
   * 图书总量
   */
  bookCount?: number;
  /**
   * 按天统计发表图书统计
   */
  bookPublishTotalByDayList?: BookPublishTotalByDay[];
  /**
   * 按月统计发表图书统计
   */
  bookPublishTotalByMonthList?: BookPublishTotalByMonth[];
  /**
   * 资源总数
   */
  resourceCount?: number;
  /**
   * 总评论数
   */
  totalCommentCount?: number;
  /**
   * 总下载量
   */
  totalDownloadCount?: number;
  /**
   * 用户数量
   */
  userCount?: number;
}

/**
 * 首页统计数据
 *
 * BookCategoryTotal
 */
export interface BookCategoryTotal {
  /**
   * 发表图书总量
   */
  bookCount?: number;
  /**
   * 分类id
   */
  categoryId?: string;
  /**
   * 分类名称
   */
  categoryName?: string;
}

/**
 * 首页统计数据
 *
 * BookPublishTotalByDay
 */
export interface BookPublishTotalByDay {
  /**
   * 日期
   */
  date?: string;
  /**
   * 发表图书总量
   */
  total?: number;
}

/**
 * 首页统计数据
 *
 * BookPublishTotalByMonth
 */
export interface BookPublishTotalByMonth {
  /**
   * 日期
   */
  date?: string;
  /**
   * 发表图书总量
   */
  total?: number;
}
