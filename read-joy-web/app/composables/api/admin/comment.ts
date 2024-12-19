import type { CommentStatusEnum } from "../comment";
import type { IPage } from "../types";
import type { BoolEnum, Result } from "../types/result";
import { useHttp } from "../../utils/useHttp";

/**
 * 获取评论列表(分页) （管理员）
 * @param dto 条件
 * @param token 用户token
 * @returns 分页
 */
export function useAdminCommentPageByDTOApi(dto: AdminSelectCommentPageDTO, token: string) {
  return useHttp.get<Result<IPage<AdminCommentVO>>>(
    "/admin/comment/page",
    {
      ...dto,
    },
    {
      headers: { Authorization: token },
    },
  );
}

/**
 * 修改评论审核状态
 * @param commentId 评论ID
 * @param status 审核状态
 * @param token 用户token
 * @returns 是否成功
 */
export function useAdminUpdateCommentStatusApi(commentId: number, status: CommentStatusEnum, token: string) {
  return useHttp.put<Result<number>>(
    `/admin/comment/${commentId}/${status}`,
    {},
    {
      headers: { Authorization: token },
    },
  );
}

/**
 * 删除评论（管理员）
 * @param commentId 评论ID
 * @param token 用户token
 * @returns 是否成功
 */
export function useAdminDeleteCommentApi(commentId: number, token: string) {
  return useHttp.deleted<Result<number>>(
    `/admin/comment/${commentId}`,
    {},
    {
      headers: { Authorization: token },
    },
  );
}


/**
 * 批量删除评论（管理员）
 * @param ids 评论ID数组
 * @param token 用户token
 * @returns 删除条数
 */
export function useAdminBatchDeleteCommentApi(ids: number[], token: string) {
  return useHttp.deleted<Result<number>>(
    "/admin/comment/batch",
    {
      ids,
    },
    {
      headers: { Authorization: token },
    },
  );
}

export interface AdminSelectCommentPageDTO {
  /**
   * 页码
   */
  page: number;
  /**
   * 每页条数
   */
  size: number;
  /**
   * 书籍ID
   */
  bookId?: number;
  /**
   * 用户ID
   */
  commentator?: number;
  /**
   * 结束时间(精度：天)
   */
  endDateTime?: string;
  /**
   * 是否删除 0-未删除 1-已删除
   */
  isDeleted?: number;
  /**
   * 关键字
   */
  keyword?: string;
  /**
   * 创建时间排序 1-降序 desc 2-升序 asc
   */
  sortOrder?: number;
  /**
   * 开始时间(精度：天)
   */
  startDateTime?: string;
  /**
   * 评论的审核状态：1待审核，2已审核，3已驳回
   */
  status?: CommentStatusEnum;
}


/**
 * 评论展示VO
 */
export interface AdminCommentVO {
  /**
   * 评论ID
   */
  id: number;
  /**
   * 书籍ID
   */
  bookId: number;
  bookTitle?: string;
  /**
   * 评论者ID
   */
  commentator: number;
  /**
   * 用户头像
   */
  commentatorAvatar: string;
  /**
   * username
   * 用户昵称
   */
  commentatorName: string;
  /**
   * 评论内容
   */
  commentBody: string;
  /**
   * 评论状态：1待审核 2已审核 3已驳回
   */
  commentStatus: CommentStatusEnum;
  /**
   * 审核状态文本
   */
  commentStatusText: string;
  /**
   * 创建时间
   */
  createTime: string;
  /**
   * 是否删除
   * 是否已删除：0为否，1为是
   */
  isDeleted: BoolEnum;
}


