import type { IPage } from "../types";
import type { DefaultOrderSort, Result } from "../types/result";
import { useHttp } from "../../utils/useHttp";

/**
 * 获取图书列表(分页) （管理员）
 * @param dto 条件
 * @param token 用户token
 * @returns 分页
 */
export function useAdminBookPageByDTOApi(dto: AdminSelectBookPageDTO, token: string) {
  return useHttp.get<Result<IPage<AdminBookVO>>>(
    "/admin/book/page",
    {
      ...dto,
    },
    {
      headers: { Authorization: token },
    },
  );
}
/**
 * 获取图书详情（管理员）
 * @param bookId 图书ID
 * @param token 用户token
 * @returns 图书详情
 */
export function useAdminBookDetailApi(bookId: number, token: string) {
  return useHttp.get<Result<AdminBookDetailVO>>(`/admin/book/${bookId}`, {}, {
    headers: { Authorization: token },
  });
}
/**
 * 添加图书（管理员） multipart/form-data
 * @param dto 图书信息
 * @param token 用户token
 * @returns 图书ID
 */
export function useAdminAddBookApi(dto: FormData | InsertBookDTO, token: string) {
  return useHttp.post_formdata<Result<number>>(
    "/admin/book",
    dto,
    {
      headers: { Authorization: token },
    },
  );
}

/**
 * 修改图书（管理员） multipart/form-data
 * @param bookId 图书ID
 * @param dto 图书信息
 * @param token 用户token
 * @returns 图书ID
 */
export function useAdminUpdateBookApi(bookId: number, dto: FormData | UpdateBookDTO, token: string) {
  return useHttp.put_formdata<Result<number>>(
    `/admin/book/${bookId}`,
    dto,
    {
      headers: { Authorization: token },
    },
  );
}

/**
 * 删除图书（管理员）
 * @param bookId 图书ID
 * @param token 用户token
 * @returns 图书ID
 */
export function useAdminDeleteBookApi(bookId: number, token: string) {
  return useHttp.deleted<Result<number>>(`/admin/book/${bookId}`, {}, {
    headers: { Authorization: token },
  });
}

/**
 * 批量删除图书（管理员）
 * @param ids 图书ID数组
 * @param token 用户token
 * @returns 图书ID数组
 */
export function useAdminBatchDeleteBookApi(ids: number[], token: string) {
  return useHttp.deleted<Result<number>>(
    "/admin/book/batch",
    {
      ids,
    },
    {
      headers: { Authorization: token },
    },
  );
}


export interface InsertBookDTO {
  /** 国际标准书号 */
  isbn: string;

  /** 书名 */
  title: string;

  /** 分类ID */
  categoryId: number;

  /** 作者 */
  author: string;

  /** 价格 */
  price: number;

  /** 出版社 */
  publisher: string;

  /** 封面图片 */
  coverImage: File; // 假设 MultipartFile 可以映射为 File 类型

  /** 出版日期 */
  publishionDate: string; // 格式：yyyy-MM-dd

  /** 书籍介绍 */
  introduction?: string;
}

export interface UpdateBookDTO {
  /**
   * 国际标准书号
   * @example "978-3-16-148410-0"
   */
  isbn: string; // ISBN长度必须在1到20之间！

  /**
   * 书名
   * @example "百年孤独"
   */
  title: string; // 书名长度必须在1到100之间！

  /**
   * 分类ID
   * @example 1
   */
  categoryId: number; // 分类ID必须大于0！

  /**
   * 作者
   * @example "加西亚·马尔克斯"
   */
  author: string; // 作者长度必须在1到100之间！

  /**
   * 书籍介绍
   * @example "魔幻现实主义文学的代表作"
   */
  introduction: string; // 书籍介绍长度不超过1000字！

  /**
   * 价格
   */
  price: number; // 价格必须大于0！

  /**
   * 出版社
   * @example "南海出版公司"
   */
  publisher: string; // 出版社长度必须在1到50之间！

  /**
   * 封面图片URL
   * @example "cover1.jpg"
   */
  coverImage?: File; // 可选属性，可能为null

  /**
   * 出版日期
   * @example "2024-01-01"
   */
  publishionDate: string; // 应为yyyy-MM-dd格式的字符串
}

export interface AdminSelectBookPageDTO {
  /**
   * 页码
   */
  page: number;
  /**
   * 每页条数
   */
  size: number;
  /**
   * 分类id
   */
  categoryId?: number;
  /**
   * 结束时间(精度：天)
   */
  endDate?: string;
  /**
   * 关键字
   */
  keyword?: string;
  /**
   * 排序方式 1-按发布时间 2-按价格
   */
  sortType?: AdminBookSortType;
  /**
   * 排序方式 1-降序 desc 2-升序 asc
   */
  sortOrder?: DefaultOrderSort;
  /**
   * 开始时间(精度：天)
   */
  startDate?: string;
}

/**
 * AdminBookDetailVO
 */
export interface AdminBookDetailVO {
  /**
   * 作者
   */
  author?: string;
  /**
   * 书籍ID
   */
  bookId: number;
  /**
   * 分类ID
   */
  categoryId: number;
  /**
   * 分类名称
   */
  categoryName: string;
  /**
   * 封面图片URL
   */
  coverImageUrl: string;
  /**
   * 书籍介绍
   */
  introduction?: string;
  /**
   * 国际标准书号
   */
  isbn: string;
  /**
   * 价格
   */
  price: number;
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
  resourceList?: AdminResourceVO[];
  /**
   * 书名
   */
  title?: string;
  [property: string]: any;
}

/**
 * 资源VO对象
 *
 * AdminResourceVO
 */
export interface AdminResourceVO {
  /**
   * 关联的图书ID
   */
  bookId: number;
  /**
   * 关联的图书标题
   */
  bookTitle: string;
  /**
   * 创建时间
   */
  createTime: string;
  /**
   * 下载数
   */
  downloadCount: number;
  /**
   * 是否删除（0 - 否，1 - 是）
   */
  isDeleted: number;
  /**
   * 点赞数
   */
  likeCount: number;
  /**
   * 资源ID
   */
  resourceId: number;
  /**
   * 资源大小（单位：字节）
   */
  size?: number;
  /**
   * 资源提交者
   */
  submitter?: string;
  /**
   * 资源标题
   */
  title: string;
  /**
   * 资源类型：application/pdf, image/jpeg, video/mp4, audio/mp3, 等
   */
  type: string;
  /**
   * 资源保存地址
   */
  url: string;
}

export enum AdminBookSortType {
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
export interface AdminBookVO {
  /**
   * 作者
   */
  author?: string;
  /**
   * 书籍ID
   */
  bookId: number;
  /**
   * 分类ID
   */
  categoryId: number;
  /**
   * 分类名称
   */
  categoryName: string;
  /**
   * 封面图片URL
   */
  coverImageUrl: string;
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
  price: number;
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
  bookId: number;
  /**
   * 创建时间
   */
  createTime: string;
  /**
   * 下载数
   */
  downloadCount: number;
  /**
   * 是否删除
   */
  isDeleted: number;
  /**
   * 点赞数
   */
  likeCount: number;
  /**
   * 资源ID
   */
  resourceId: number;
  /**
   * 资源大小
   */
  size: number;
  /**
   * 资源提交者
   */
  submitter?: string;
  /**
   * 资源标题
   */
  title: string;
  /**
   * 资源类型，视频，教学大纲，课件等
   */
  type: string;
  /**
   * 资源保存地址
   */
  url: string;
}

