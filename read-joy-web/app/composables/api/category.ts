import type { Result } from "./types/result";
import { useHttp } from "../utils/useHttp";

/**
 * 获取图书列表(分页)
 * @param dto 条件
 * @returns 分页
 */
export function getCategoryListByDTOApi(dto: SelectCategoryListDTO) {
  return useHttp.get<Result<CategoryVO[]>>(
    "/book/category/list",
    {
      ...dto,
    },
    {
    },
  );
}
export interface SelectCategoryListDTO {
  /**
   * 关键字
   */
  keyword?: string;
}


/**
 * 图书展示VO
 */
export interface CategoryVO {
  /**
   * 分类ID
   */
  categoryId?: number;
  /**
   * 分类名称
   */
  categoryName?: string;

  bookCount?: number;
}
