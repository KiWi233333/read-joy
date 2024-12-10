package com.readjoy.readjoyapi.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.readjoy.readjoyapi.common.pojo.Comment;
import com.readjoy.readjoyapi.service.CommentService;
import com.readjoy.readjoyapi.common.mapper.CommentMapper;
import org.springframework.stereotype.Service;

/**
* @author 13296
* @description 针对表【comment(评论表)】的数据库操作Service实现
* @createDate 2024-12-07 17:55:09
*/
@Service
public class CommentServiceImpl extends ServiceImpl<CommentMapper, Comment>
    implements CommentService{

}




