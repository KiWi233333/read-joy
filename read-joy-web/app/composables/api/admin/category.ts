import type { IPage } from "../types";
import type { DefaultOrderSort, Result } from "../types/result";
import { useHttp } from "../../utils/useHttp";

/**
 * 获取分类列表(分页) （管理员）
 * @param dto 条件
 * @param token 用户token
 * @returns 分页
 */
export function useAdminCategoryPageByDTOApi(dto: AdminSelectCategoryListDTO, token: string) {
  return useHttp.get<Result<IPage<AdminCategoryVO>>>(
    "/admin/book/category/page",
    {
      ...dto,
    },
    {
      headers: { Authorization: token },
    },
  );
}

/**
 * 新增分类（管理员）
 * @param dto 分类信息
 * @param token 用户token
 * @returns 是否成功
 */
export function useAdminInsertCategoryApi(dto: InsertCategoryDTO, token: string) {
  return useHttp.post<Result<number>>(
    "/admin/book/category",
    dto,
    {
      headers: { Authorization: token },
    },
  );
}

/**
 * 修改分类（管理员）
 * @param categoryId 分类ID
 * @param dto 分类信息
 * @param token 用户token
 * @returns 是否成功
 */
export function useAdminUpdateCategoryApi(categoryId: number, dto: UpdateCategoryDTO, token: string) {
  return useHttp.put<Result<number>>(
    `/admin/book/category/${categoryId}`,
    dto,
    {
      headers: { Authorization: token },
    },
  );
}

/**
 * 删除分类（管理员）
 * @param categoryId 分类ID
 * @param token 用户token
 * @returns 是否成功
 */
export function useAdminDeleteCategoryApi(categoryId: number, token: string) {
  return useHttp.deleted<Result<number>>(
    `/admin/book/category/${categoryId}`,
    {},
    {
      headers: { Authorization: token },
    },
  );
}


/**
 * 批量删除分类（管理员）
 * @param ids 分类ID数组
 * @param token 用户token
 * @returns 删除条数
 */
export function useAdminBatchDeleteCategoryApi(ids: number[], token: string) {
  return useHttp.deleted<Result<number>>(
    "/admin/book/category/batch",
    {
      ids,
    },
    {
      headers: { Authorization: token },
    },
  );
}

export interface AdminSelectCategoryListDTO {
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
}


export interface InsertCategoryDTO {

  /**
   * 分类名
   */
  categoryName: string;
}

export interface UpdateCategoryDTO {
  /**
   * 分类名
   */
  categoryName?: string;
}

export interface AdminSelectCategoryPageDTO {
  /**
   * 页码
   */
  page: number;
  /**
   * 每页条数
   */
  size: number;
  /**
   * 关键字
   */
  keyword?: string;

  idSort?: DefaultOrderSort
}


/**
 * 分类展示VO
 */
export interface AdminCategoryVO {
  /**
   * 分类ID
   */
  categoryId: number;
  /**
   * 分类名称
   */
  categoryName: string;

  bookCount?: number;
}


