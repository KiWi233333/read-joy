import type { IPage } from "./types";
import type { Result } from "./types/result";
import { useHttp } from "../utils/useHttp";

/**
 * 获取图书列表(分页)
 * @param dto 条件
 * @returns 分页
 */
export function getBookPageByDTOApi(dto: SelectBookPageDTO) {
  return useHttp.get<Result<IPage<BookVO>>>(
    "/book/page",
    {
      ...dto,
    },
    {
    },
  );
}
export interface SelectBookPageDTO {
  page: number;
  size: number;

  /**
   * 分类id
   */
  categoryId?: number;
  /**
   * 关键字
   */
  keyword?: string;
  /**
   * 排序方式 1-降序 desc 2-升序 asc
   */
  sortOrder?: BookSortOrder;
  /**
   * 排序方式 1-按发布时间 2-按价格
   */
  sortType?: BookSortType;
  /**
   * 开始时间(精度：天)
   */
  startDate?: string;
  /**
   * 结束时间(精度：天)
   */
  endDate?: string;
}
export enum BookSortOrder {
  /**
   * 降序
   */
  DESC = 1,
  /**
   * 升序
   */
  ASC = 2,
}
export enum BookSortType {
  /**
   * 按发布时间
   */
  PUBLISH_TIME = 1,
  /**
   * 按价格
   */
  PRICE = 2,
}


/**
 * 图书展示VO
 */
export interface BookVO {
  /**
   * 作者
   */
  author?: string;
  /**
   * 书籍ID
   */
  bookId?: number;
  /**
   * 分类ID
   */
  categoryId?: number;
  /**
   * 分类名称
   */
  categoryName?: string;
  /**
   * 封面图片URL
   */
  coverImageUrl?: string;
  /**
   * 书籍介绍
   */
  introduction?: string;
  /**
   * 国际标准书号
   */
  isbn?: string;
  /**
   * 价格
   */
  price?: number;
  /**
   * 出版社
   */
  publisher?: string;
  /**
   * 出版日期
   */
  publishionDate?: string;
  /**
   * 书名
   */
  title?: string;
  [property: string]: any;
}

/**
 * 获取图书详情信息
 * @param bookId 图书ID
 * @returns 图书详情
 */
export function getBookDetailApi(bookId: number) {
  return useHttp.get<Result<BookDetailVO>>(`/book/${bookId}`, {
    bookId,
  });
}


/**
 * 返回数据
 *
 * BookDetailVO
 */
export interface BookDetailVO {
  /**
   * 作者
   */
  author?: string;
  /**
   * 书籍ID
   */
  bookId?: number;
  /**
   * 分类ID
   */
  categoryId?: number;
  /**
   * 分类名称
   */
  categoryName?: string;
  /**
   * 封面图片URL
   */
  coverImageUrl?: string;
  /**
   * 书籍介绍
   */
  introduction?: string;
  /**
   * 国际标准书号
   */
  isbn?: string;
  /**
   * 价格
   */
  price?: number;
  /**
   * 出版社
   */
  publisher?: string;
  /**
   * 出版日期
   */
  publishionDate?: string;
  /**
   * 资源列表
   */
  resourceList?: ResourceVO[];
  /**
   * 书名
   */
  title?: string;
}

/**
 * 资源VO对象
 *
 * ResourceVO
 */
export interface ResourceVO {
  /**
   * 关联的图书ID
   */
  bookId?: number;
  /**
   * 创建时间
   */
  createTime?: string;
  /**
   * 下载数
   */
  downloadCount?: number;
  /**
   * 是否删除
   */
  isDeleted?: number;
  /**
   * 点赞数
   */
  likeCount?: number;
  /**
   * 资源ID
   */
  resourceId?: number;
  /**
   * 资源大小
   */
  size?: number;
  /**
   * 资源提交者
   */
  submitter?: string;
  /**
   * 资源标题
   */
  title?: string;
  /**
   * 资源类型，视频，教学大纲，课件等
   */
  type?: string;
  /**
   * 资源保存地址
   */
  url?: string;
  [property: string]: any;
}
