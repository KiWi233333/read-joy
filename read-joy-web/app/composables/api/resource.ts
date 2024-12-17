import type { BoolEnum, Result } from "./types/result";
import { useHttp } from "../utils/useHttp";


/**
 * 点赞资源
 * @param resourceId 资源id
 * @param token 用户token
 * @returns 是否成功
 */
export function useAddResourceLikeApi(resourceId: number, token: string) {
  return useHttp.put<Result<BoolEnum>>(`/book/resource/like/${resourceId}`, {}, {
    headers: {
      Authorization: token,
    },
  });
}
