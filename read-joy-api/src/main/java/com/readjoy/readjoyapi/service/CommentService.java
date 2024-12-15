package com.readjoy.readjoyapi.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.readjoy.readjoyapi.common.dto.comment.AdminSelectCommentDTO;
import com.readjoy.readjoyapi.common.dto.comment.InsertCommentDTO;
import com.readjoy.readjoyapi.common.vo.comment.CommentVO;

/**
 * 评论服务层
 *
 * @author 13296
 * @description 针对表【comment(评论表)】的数据库操作Service
 * @createDate 2024-12-07 17:55:09
 */
public interface CommentService {

    /**
     * 添加评论
     *
     * @param dto 评论DTO
     * @return 评论VO
     */
    CommentVO addComment(InsertCommentDTO dto);

    /**
     * 删除评论
     *
     * @param userId 用户id
     * @param id     评论id
     * @return 删除结果
     */
    Integer deleteComment(Integer userId, Integer id);


    /**
     * 管理员获取评论分页（分页查询）
     *
     * @param aDTO 管理员查询评论DTO
     * @return 分页结果
     */
    IPage<CommentVO> getPageByDTO(AdminSelectCommentDTO aDTO);

    /**
     * 更新评论状态
     *
     * @param id     评论id
     * @param status 状态
     * @return 更新结果
     */
    Integer updateCommentStatus(Integer id, Integer status);
}
