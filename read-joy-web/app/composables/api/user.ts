import type { BoolEnum, Result } from "./types/result";
import { useHttp } from "../utils/useHttp";

/**
 * 用户注册
 * @param dto 参数
 * @returns 用户注册信息
 */
export function useRegisterApi(dto: UserRegisterDTO) {
  return useHttp.post<Result<UserRegisterVO>>("/user/register", {
    ...dto,
  });
}

/**
 * 用户登录 (返回用户信息)
 * @param dto 参数
 * @returns 用户登录信息
 */
export function useLoginApi(dto: UserLoginDTO) {
  return useHttp.post<Result<UserLoginVO>>("/user/login", {
    ...dto,
  });
}


/**
 * 获取用户信息
 * @param token 参数
 * @returns 用户信息
 */
export function useGetUserInfoApi(token: string) {
  return useHttp.get<Result<UserInfoVO>>("/user", {
  }, {
    headers: {
      Authorization: token,
    },
  });
}

/**
 * 修改密码
 * @param dto 参数
 * @param token token
 * @returns 是否成功
 */
export function useUpdateUserPwdApi(dto: UserUpdatePwdDTO, token: string) {
  return useHttp.put<Result<BoolEnum>>("/user/pwd", {
    ...dto,
  }, { headers: { Authorization: token } });
}

/**
 * 修改用户信息
 * @param dto 参数
 * @returns 是否成功
 */
export function updateUserInfoApi(dto: FormData, token: string) {
  return useHttp.put_formdata<Result<UserInfoVO>>("/user", dto, {
    headers: {
      Authorization: token,
    },
  });
}


/**
 * UserUpdatePwdDTO
 */
export interface UserUpdatePwdDTO {
  newPassword: string;
  oldPassword: string;
}

export interface UserRegisterVO extends UserLoginVO {

}
/**
 * UserRegisterDTO
 */
export interface UserRegisterDTO {
  /**
   * 头像URL
   */
  cover?: string;
  loginName: string;
  loginPassword: string;
  /**
   * 电话号码
   */
  telephone?: string;
  /**
   * 真实姓名
   */
  trueName?: string;
}

/**
 * UserLoginDTO
 */
export interface UserLoginDTO {
  loginName: string;
  password: string;
}

/**
 * 返回数据
 *
 * UserLoginVO
 */
export interface UserLoginVO {
  /**
   * token
   */
  token: string;
  /**
   * 用户信息
   */
  userInfo: UserInfoVO;
}

/**
 * 用户信息
 *
 * UserInfoVO
 */
export interface UserInfoVO {
  /**
   * 创建时间
   */
  createTime?: string;
  /**
   * 用户ID
   */
  id?: number;
  /**
   * 用户头像URL
   */
  imgUrl?: string;
  /**
   * 是否审核通过
   */
  isChecked?: BoolEnum;
  /**
   * 登录名
   */
  loginName?: string;
  /**
   * 电话号码
   */
  telephone?: string;
  /**
   * 真实姓名
   */
  trueName?: string;
  /**
   * 用户类型
   */
  userType?: CustomerUserType;
}

export enum CustomerUserType {
  /**
   * 普通用户
   */
  CUSTOMER = 1,
}


/**
 * UserUpdateInfoDTO
 */
export interface UserUpdateInfoDTO {
  /**
   * 头像URL
   */
  cover?: File;

  loginName?: string;
  /**
   * 电话号码
   */
  telephone?: string;
  /**
   * 真实姓名
   */
  trueName?: string;
}
