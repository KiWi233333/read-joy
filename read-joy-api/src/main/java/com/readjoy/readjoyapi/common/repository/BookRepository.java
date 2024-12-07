package com.readjoy.readjoyapi.common.repository;

import com.baomidou.mybatisplus.extension.repository.CrudRepository;
import com.readjoy.readjoyapi.common.mapper.BookMapper;
import com.readjoy.readjoyapi.common.pojo.Book;
import org.springframework.stereotype.Component;

/**
 * @className: BookRepository
 * @author: Kiwi23333
 * @description: TODO描述
 * @date: 2024/12/7 18:22
 */
@Component
public class BookRepository extends CrudRepository<BookMapper, Book> {

}
