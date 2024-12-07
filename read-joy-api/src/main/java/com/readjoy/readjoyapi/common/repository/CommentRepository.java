package com.readjoy.readjoyapi.common.repository;

import com.baomidou.mybatisplus.extension.repository.CrudRepository;
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
public class CommentRepository extends CrudRepository<CommentMapper, Comment> {

}
