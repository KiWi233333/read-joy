import type { IPage } from "../types";
import type { BoolEnum, DefaultOrderSort, Result } from "../types/result";
import { useHttp } from "../../utils/useHttp";

/**
 * 获取资源列表(分页) （管理员）
 * @param dto 条件
 * @param token 用户token
 * @returns 分页
 */
export function useAdminResourcePageByDTOApi(dto: AdminSelectResourcePageDTO, token: string) {
  return useHttp.get<Result<IPage<AdminResourceVO>>>(
    "/admin/book/resource/page",
    {
      ...dto,
    },
    {
      headers: { Authorization: token },
    },
  );
}
/**
 * 获取资源详情（管理员）
 * @param resourceId 资源ID
 * @param token 用户token
 * @returns 资源详情
 */
export function useAdminResourceDetailApi(resourceId: number, token: string) {
  return useHttp.get<Result<AdminResourceVO>>(`/admin/book/resource/${resourceId}`, {}, {
    headers: { Authorization: token },
  });
}
/**
 * 添加资源（管理员） multipart/form-data
 * @param dto 资源信息
 * @param token 用户token
 * @returns 资源ID
 */
export function useAdminAddResourceApi(dto: FormData | InsertResourceDTO, token: string) {
  return useHttp.post_formdata<Result<number>>(
    "/admin/book/resource",
    dto,
    {
      headers: { Authorization: token },
    },
  );
}

/**
 * 修改资源（管理员） multipart/form-data
 * @param resourceId 资源ID
 * @param dto 资源信息
 * @param token 用户token
 * @returns 资源ID
 */
export function useAdminUpdateResourceApi(resourceId: number, dto: FormData | UpdateResourceDTO, token: string) {
  return useHttp.put_formdata<Result<number>>(
    `/admin/book/resource/${resourceId}`,
    dto,
    {
      headers: { Authorization: token },
    },
  );
}

/**
 * 删除资源（管理员）
 * @param resourceId 资源ID
 * @param token 用户token
 * @returns 删除条数
 */
export function useAdminDeleteResourceApi(resourceId: number, token: string) {
  return useHttp.deleted<Result<number>>(`/admin/book/resource/${resourceId}`, {}, {
    headers: { Authorization: token },
  });
}

/**
 * 批量删除资源（管理员）
 * @param ids 资源ID数组
 * @param token 用户token
 * @returns 删除条数
 */
export function useAdminBatchDeleteResourceApi(ids: number[], token: string) {
  return useHttp.deleted<Result<number>>(
    "/admin/book/resource/batch",
    {
      ids,
    },
    {
      headers: { Authorization: token },
    },
  );
}


export interface AdminSelectResourcePageDTO {
  /**
   * 分类id
   */
  bookId?: number;
  /**
   * 结束时间(精度：天)
   */
  endDate?: string;
  /**
   * 是否删除（0 - 否，1 - 是）
   */
  isDeleted?: BoolEnum;
  /**
   * 关键字
   */
  keyword?: string;
  /**
   * 页码
   */
  page: number;
  /**
   * 每页条数
   */
  size: number;
  /**
   * 排序方式 1-降序 desc 2-升序 asc
   */
  sortOrder?: number;
  /**
   * 排序方式 1-按发布时间 2-按下载量 3-按点赞
   */
  sortType?: AdminResourceSortType;
  /**
   * 开始时间(精度：天)
   */
  startDate?: string;
}

// 排序方式 1-按发布时间 2-按下载量 3-按点赞
export enum AdminResourceSortType {
  /**
   * 按发布时间
   */
  CREATE_TIME = 1,
  /**
   * 按下载量
   */
  DOWNLOAD_COUNT = 2,
  /**
   * 按点赞
   */
  LIKE_COUNT = 3,
}

/**
 * 资源VO对象
 *
 * AdminResourceVO
 */
export interface AdminResourceVO {
  /**
   * 资源ID
   */
  resourceId: number;
  /**
   * 资源提交者
   */
  submitter: string;
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
   * 资源大小（单位：字节）
   */
  size?: number;
}

export interface InsertResourceDTO {
  bookId: number;
  title: string;
  type: string;
  resourceFile: File;
  isDeleted: BoolEnum;
}

export interface UpdateResourceDTO {
  bookId?: number;
  title?: string;
  type?: string;
  resourceFile?: File;
  isDeleted?: BoolEnum;
}
