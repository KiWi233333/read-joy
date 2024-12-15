import type { IPage } from "./types";
import type { BoolEnum, Result } from "./types/result";
import { useHttp } from "../utils/useHttp";

/**
 * 获取评论列表(分页)
 * @param bookId 书籍id
 * @param dto 条件
 * @param token token
 * @returns 分页
 */
export function getCommentPageByDTOApi(bookId: number, dto: SelectCommentPageDTO, token: string) {
  return useHttp.get<Result<IPage<CommentVO>>>(
    `/comment/${bookId}`,
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
 * 添加评论
 * @param dto 条件
 * @returns 评论vo
 */
export function addCommentApi(dto: InsertCommentDTO, token: string) {
  return useHttp.post<Result<CommentVO>>(
    "/comment",
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
 * 删除评论
 * @param id 评论id
 * @param token token
 * @returns 是否成功
 */
export function deleteCommentApi(id: number, token: string) {
  return useHttp.deleted<Result<number>>(
    `/comment/${id}`,
    {
    },
    {
      headers: {
        Authorization: token,
      },
    },
  );
}

export interface InsertCommentDTO {
  /**
   * 书籍ID
   */
  bookId: number;
  /**
   * 评论内容
   */
  commentBody: string;
}

export interface SelectCommentPageDTO {
  /**
   * 页码
   */
  page: number;
  /**
   * 每页条数
   */
  size: number;
  /**
   * 结束时间
   */
  endDateTime?: string;
  /**
   * 只看自己
   */
  onlyMine?: BoolEnum;
  /**
   * 创建时间排序 1-降序 desc 2-升序 asc
   */
  sortOrder?: CommentSortOrder;
  /**
   * 开始时间
   */
  startDateTime?: string;
  [property: string]: any;
}

export enum CommentSortOrder {
  /**
   * 降序
   */
  DESC = 1,
  /**
   * 升序
   */
  ASC = 2,
}


/**
 * 评论VO对象
 *
 * CommentVO
 */
export interface CommentVO {
  /**
   * 书籍ID
   */
  bookId?: number;
  /**
   * 评论者ID
   */
  commentator?: number;
  /**
   * 用户头像
   */
  commentatorAvatar?: string;
  /**
   * username
   * 用户昵称
   */
  commentatorName?: string;
  /**
   * 评论内容
   */
  commentBody?: string;
  /**
   * 审核状态：1待审核，2已审核，3已驳回
   */
  commentStatus?: CommentStatusEnum;
  /**
   * 审核状态文本
   */
  commentStatusText?: string;
  /**
   * 创建时间
   */
  createTime?: string;
  /**
   * 评论ID
   */
  id?: number;
  /**
   * 是否删除
   * 是否已删除：0为否，1为是
   */
  isDeleted?: BoolEnum;
}

export enum CommentStatusEnum {
  /**
   * 待审核
   */
  PENDING = 1,
  /**
   * 已审核
   */
  APPROVED = 2,
  /**
   * 已驳回
   */
  REJECTED = 3,
}
