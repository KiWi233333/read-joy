package com.readjoy.readjoyapi.repository;

import com.github.yulichang.repository.JoinCrudRepository;
import com.readjoy.readjoyapi.common.mapper.CommentMapper;
import com.readjoy.readjoyapi.common.pojo.Comment;
import org.springframework.stereotype.Component;

/**
 * @className: CommentRepository
 * @author: Kiwi23333
 * @description: TODO描述
 * @date: 2024/12/7 18:22
 */
@Component
public class CommentRepository extends JoinCrudRepository<CommentMapper, Comment> {

}
