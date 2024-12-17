package com.readjoy.readjoyapi.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.readjoy.readjoyapi.common.dto.comment.AdminSelectCommentDTO;
import com.readjoy.readjoyapi.common.dto.comment.InsertCommentDTO;
import com.readjoy.readjoyapi.common.enums.BoolEnum;
import com.readjoy.readjoyapi.common.mapper.CommentMapper;
import com.readjoy.readjoyapi.common.pojo.Comment;
import com.readjoy.readjoyapi.common.utils.AssertUtil;
import com.readjoy.readjoyapi.common.utils.RequestHolderUtil;
import com.readjoy.readjoyapi.common.vo.comment.CommentVO;
import com.readjoy.readjoyapi.repository.CommentRepository;
import com.readjoy.readjoyapi.service.CommentService;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

/**
 * CommentService 实现类
 */
@Service
public class CommentServiceImpl extends ServiceImpl<CommentMapper, Comment> implements CommentService {

    @Resource
    private CommentRepository commentRepository;


    /**
     * 获取评论分页（分页查询） 通用 注意部分权限
     *
     * @param aDTO 管理员查询评论DTO
     * @return 分页结果
     */
    @Override
    public IPage<CommentVO> getPageByDTO(AdminSelectCommentDTO aDTO) {
        return commentRepository.getPageByDTO(aDTO);
    }

    /**
     * 添加评论
     *
     * @param dto 评论DTO
     * @return 评论VO
     */
    @Override
    public CommentVO addComment(InsertCommentDTO dto) {
        final Comment entity = dto.toEntity(RequestHolderUtil.get().getId(), true);
        final boolean save = commentRepository.save(entity);
        AssertUtil.isTrue(save, "添加评论失败，请稍后再试！");
        return commentRepository.getCommentVOById(entity.getId());
    }

    /**
     * 删除评论
     *
     * @param userId 用户id
     * @param id     评论id
     * @return 删除结果
     */
    @Override
    public Integer deleteComment(Integer userId, Integer id) {
        // 逻辑删除
        final boolean success = commentRepository.update(
                new Comment().setIsDeleted(BoolEnum.TRUE.getValue()),
                new LambdaQueryWrapper<Comment>().eq(Comment::getId, id)
                        .eq(Comment::getCommentator, userId) // 评论者只能删除自己的评论
                        .eq(Comment::getIsDeleted, BoolEnum.FALSE.getValue())); // 未删除的评论才可以删除
        AssertUtil.isTrue(success, "删除评论失败，请稍后再试！");
        return 1;
    }


    /**
     * 更新评论状态
     *
     * @param id     评论id
     * @param status 状态
     * @return 更新结果
     */
    @Override
    public Integer updateCommentStatus(Integer id, Integer status) {
        final boolean success = commentRepository.updateById(new Comment()
                .setId(id)
                .setCommentStatus(status));
        AssertUtil.isTrue(success, "更新评论状态失败，请稍后再试！");
        return 1;
    }

    /**
     * 批量删除评论
     *
     * @param ids 评论id数组
     * @return 删除数量
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public Long batchDeleteComment(Integer[] ids) {
        final long length = commentRepository.getBaseMapper().update(
                new Comment().setIsDeleted(BoolEnum.TRUE.getValue()),
                new LambdaQueryWrapper<Comment>().in(Comment::getId, List.of(ids))
                        .eq(Comment::getIsDeleted, BoolEnum.FALSE.getValue())); // 未删除的评论才可以删除
        AssertUtil.isTrue(length == ids.length, "批量删除评论失败，请稍后再试！");
        return length;
    }
}




