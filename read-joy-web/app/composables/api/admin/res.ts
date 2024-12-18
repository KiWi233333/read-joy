import type { BoolEnum, Result } from "../types/result";
import { useHttp } from "../../utils/useHttp";


/**
 * 获取首页统计数据
 * @param dto
 * @param token
 * @returns Result<SelectHomeStatisticsDto>
 */
export function selectHomeStatistics(dto: SelectHomeStatisticsDto, token: string) {
  return useHttp.get<Result<AdminHomeStatisticsVO>>("/admin/res/statistics/home", dto, { headers: { Authorization: token } });
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
   * 用户数量
   */
  userCount: number;
  /**
   * 图书总量
   */
  bookCount: number;
  /**
   * 分类总量
   */
  bookCategoryCount: number;
  /**
   * 资源总数
   */
  resourceCount: number;
  /**
   * 总评论数
   */
  totalCommentCount: number;
  /**
   * 待审核评论数
   */
  pendingCommentCount: number,
  /**
   * 已驳回评论数
   */
  rejectionCommentCount: number,
  /**
   * 总下载量
   */
  totalDownloadCount: number;

  /**
   * 按类别图书总量统计
   */
  bookCategoryTotalList?: BookCategoryTotal[];
  /**
   * 按天统计发表图书统计
   */
  bookPublishTotalByDayList: BookPublishTotal[];
  /**
   * 按月统计发表图书统计
   */
  bookPublishTotalByMonthList: BookPublishTotal[];
  bookPublishTotalByYearList: BookPublishTotal[];
  /**
   * 时间
   */
  requestTime: string;
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
  bookCount: number;
  /**
   * 分类id
   */
  categoryId: string;
  /**
   * 分类名称
   */
  categoryName: string;
}

/**
 * 首页统计数据
 *
 * BookPublishTotal
 */
export interface BookPublishTotal {
  /**
   * 日期
   */
  date: string;
  /**
   * 发表图书总量
   */
  total: number;
}
