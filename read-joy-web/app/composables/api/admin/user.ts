import type { IPage } from "../types";
import type { BoolEnum, DefaultOrderSort, Result } from "../types/result";
import { useHttp } from "~/composables/utils/useHttp";
/**
 * 获取用户列表(分页)
 * @param dto 条件
 *
 * @returns 分页
 */
export function useAdminUserPageByDTOApi(dto: AdminSelectUserPageDTO, token: string) {
  return useHttp.get<Result<IPage<AdminUserVO>>>(
    "/admin/user/page",
    {
      ...dto,
    },
    {
      headers: {
        Authorization: token,
      },
    },
  );
}

/**
 * 删除用户（批量）
 * @param ids 用户ID数组
 * @param token 登录token
 * @returns 删除结果
 */
export function useAdminUserBatchDeleteApi(ids: number[], token: string) {
  return useHttp.deleted<Result<boolean>>(
    "/admin/user/batch",
    {
      ids,
    },
    {
      headers: {
        Authorization: token,
      },
    },
  );
}

/**
 * 修改用户审核状态（禁用/启用）
 * @param id 用户ID
 * @param status 审核状态 1-通过 0-未通过
 * @param token 登录token
 * @returns 修改结果
 */
export function useAdminUserCheckedApi(id: number, status: BoolEnum, token: string) {
  return useHttp.put<Result<boolean>>(
    `/admin/user/${id}?status=${status}`,
    {
    },
    {
      headers: {
        Authorization: token,
      },
    },
  );
}

/**
 * 用户信息 VO
 *
 * UserInfoVO
 */
export interface AdminUserVO {
  /**
   * 用户ID
   */
  id: number;
  /**
   * 创建时间
   */
  createTime: string;
  /**
   * 用户头像URL
   */
  imgUrl?: string;
  /**
   * 登录名
   */
  loginName: string;
  /**
   * 用户是否审核通过，1：通过，0：未通过
   */
  isChecked: BoolEnum;
  /**
   * 电话号码
   */
  telephone?: string;
  /**
   * 真实姓名
   */
  trueName?: string;
  /**
   * 用户类型，1：普通用户
   */
  userType: number;
}

export interface AdminSelectUserPageDTO {
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
  /**
   * 创建排序 1-降序 desc 2-升序 asc
   */
  createOrder?: DefaultOrderSort;
  /**
   * 是否已审核 0-未审核 1-已审核
   */
  isChecked?: BoolEnum;
}
