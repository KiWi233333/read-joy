package com.readjoy.readjoyapi.common.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.readjoy.readjoyapi.common.pojo.Book;
import com.readjoy.readjoyapi.common.service.BookService;
import com.readjoy.readjoyapi.common.mapper.BookMapper;
import org.springframework.stereotype.Service;

/**
* @author 13296
* @description 针对表【book(书籍表)】的数据库操作Service实现
* @createDate 2024-12-07 17:55:09
*/
@Service
public class BookServiceImpl extends ServiceImpl<BookMapper, Book>
    implements BookService{

}




