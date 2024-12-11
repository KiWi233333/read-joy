// 返回值定义
export interface Result<T> {
  [x: string]: any
  code: ResultStatus
  message: string
  data: T
}
export enum ResultStatus {
  SUCCESS = 20000,
  INSERT_ERR = 20001,
  DELETE_ERR = 20002,
  SELECT_ERR = 20003,
  UPDATE_ERR = 20004,
  LINK_NULL_ERR = 20005,
  NULL_ERR = 40001,
  TOKEN_ERR = 40002,
  PARAM_ERR = 40003,
  BUSY_ERR = 40004,
  DEFAULT_ERR = 40005,
  PERMISSION_ERR = 40006,
  TOKEN_EXPIRED_ERR = 40007,
  STATUS_OFF_ERR = 40008,
  TOKEN_DEVICE_ERR = 40009,
}

export const ResultStatusText: {
  [key in ResultStatus]: string
} = {
  [ResultStatus.SUCCESS]: "操作成功",
  [ResultStatus.INSERT_ERR]: "添加或已经存在",
  [ResultStatus.DELETE_ERR]: "被删或不存在",
  [ResultStatus.SELECT_ERR]: "查询不到",
  [ResultStatus.UPDATE_ERR]: "修改失败",
  [ResultStatus.LINK_NULL_ERR]: "链接元素不存在",
  [ResultStatus.NULL_ERR]: "空值错误",
  [ResultStatus.TOKEN_ERR]: "认证失败",
  [ResultStatus.PARAM_ERR]: "参数错误",
  [ResultStatus.BUSY_ERR]: "阻塞或被占用繁忙",
  [ResultStatus.DEFAULT_ERR]: "操作错误，请重试！",
  [ResultStatus.PERMISSION_ERR]: "权限不足",
  [ResultStatus.TOKEN_EXPIRED_ERR]: "身份过期，请重新登录！",
  [ResultStatus.STATUS_OFF_ERR]: "账号被封禁，详情联系客服！",
  [ResultStatus.TOKEN_DEVICE_ERR]: "认证失败，登录设备有误！",
};


export enum Sort {
  ASC = 0,
  DESC = 1, // 降序
}
export enum isTrue {
  TRUE = 1,
  FALESE = 0,
}

export enum BoolEnum {
  TRUE = 1,
  FALSE = 0,
}
