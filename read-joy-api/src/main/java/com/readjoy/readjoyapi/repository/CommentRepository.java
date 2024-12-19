package com.readjoy.readjoyapi.repository;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.github.yulichang.repository.JoinCrudRepository;
import com.github.yulichang.wrapper.MPJLambdaWrapper;
import com.readjoy.readjoyapi.common.dto.comment.AdminSelectCommentDTO;
import com.readjoy.readjoyapi.common.mapper.CommentMapper;
import com.readjoy.readjoyapi.common.pojo.Book;
import com.readjoy.readjoyapi.common.pojo.Comment;
import com.readjoy.readjoyapi.common.pojo.User;
import com.readjoy.readjoyapi.common.vo.comment.CommentVO;
import org.springframework.stereotype.Component;

/**
 * @className: CommentRepository
 * @author: Kiwi23333
 * @description: TODO描述
 * @date: 2024/12/7 18:22
 */
@Component
public class CommentRepository extends JoinCrudRepository<CommentMapper, Comment> {

    /**
     * 通用分页查询
     *
     * @param dto 查询条件
     * @return 分页数据
     */
    public IPage<CommentVO> getPageByDTO(AdminSelectCommentDTO dto) {
        final MPJLambdaWrapper<Comment> qw = new MPJLambdaWrapper<Comment>()
                .selectAs(Comment::getId, CommentVO::getId)
                .selectAs(Comment::getCommentBody, CommentVO::getCommentBody)
                .selectAs(Comment::getCreateTime, CommentVO::getCreateTime)
                .selectAs(Comment::getCommentStatus, CommentVO::getCommentStatus)
                .selectAs(Comment::getIsDeleted, CommentVO::getIsDeleted)
                .selectAs(Comment::getBookId, CommentVO::getBookId)
                .selectAs(Comment::getCommentator, CommentVO::getCommentator)
                .selectAs(User::getLoginName, CommentVO::getCommentatorName)
                .selectAs(User::getImgUrl, CommentVO::getCommentatorAvatar)
                .eq(dto.getStatus() != null, Comment::getCommentStatus, dto.getStatus()) // 状态
                .eq(dto.getIsDeleted() != null, Comment::getIsDeleted, dto.getIsDeleted()) // 是否删除
                .eq(dto.getCommentator() != null, Comment::getCommentator, dto.getCommentator())// 评论人
                .like(StringUtils.isNotBlank(dto.getKeyword()), Comment::getCommentBody, dto.getKeyword())
                .leftJoin(User.class, User::getId, Comment::getCommentator)

                .selectAs(Book::getTitle, CommentVO::getBookTitle) // 书籍标题
                .selectAs(Book::getCoverImageUrl, CommentVO::getBookCoverUrl) // 书籍封面
                .leftJoin(Book.class, Book::getBookId, Comment::getBookId);
        // 有则链表
        if (dto.getBookId() != null) {
            qw.leftJoin(Book.class, Book::getBookId, Comment::getBookId)
                    .eq(Book::getBookId, dto.getBookId())
                    .like(StringUtils.isNotBlank(dto.getKeyword()), Book::getTitle, dto.getKeyword());
        }
        // 排序
        qw.orderBy(dto.getSortOrder() != null, dto.checkAsc(), Comment::getCreateTime);
        // 起始结束时间
        qw.between(dto.checkTimeSort(), Comment::getCreateTime, dto.getStartDateTime(), dto.getEndDateTime());
        return this.selectJoinListPage(dto.toPage(), CommentVO.class, qw);
    }

    /**
     * 根据id获取评论详情
     *
     * @param id 评论id
     * @return 评论详情
     */
    public CommentVO getCommentVOById(Integer id) {
        final MPJLambdaWrapper<Comment> qw = new MPJLambdaWrapper<>();
        qw.selectAll(Comment.class)
                .eq(Comment::getId, id)
                .selectAs(User::getLoginName, CommentVO::getCommentatorName)
                .selectAs(User::getImgUrl, CommentVO::getCommentatorAvatar)
                .leftJoin(User.class, User::getId, Comment::getCommentator);
        return this.selectJoinOne(CommentVO.class, qw);
    }
}
