import type { BoolEnum, Result } from "../types/result";
import { useHttp } from "../../utils/useHttp";

/**
 * 管理员登录 (返回管理员信息)
 * @param dto 参数
 * @returns 管理员登录信息
 */
export function useLoginApi(dto: AdminLoginDTO) {
  return useHttp.post<Result<AdminLoginVO>>("/admin/login", {
    ...dto,
  });
}

/**
 * AdminLoginDTO
 */
export interface AdminLoginDTO {
  username: string;
  password: string;
}

/**
 * 返回数据
 *
 * AdminLoginVO
 */
export interface AdminLoginVO {
  /**
   * token
   */
  token: string;
  /**
   * 管理员信息
   */
  username: string;
}

/**
 * 修改密码
 * @param dto 参数
 * @returns 是否成功
 */
export function useUpdatePwdApi(dto: AdminUpdatePwdDTO) {
  return useHttp.put<Result<BoolEnum>>("/user/pwd", {
    ...dto,
  });
}
/**
 * AdminUpdatePwdDTO
 */
export interface AdminUpdatePwdDTO {
  newPassword: string;
  oldPassword: string;
}
