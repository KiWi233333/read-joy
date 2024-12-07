package com.readjoy.readjoyapi.common.mapper;

import com.github.yulichang.base.MPJBaseMapper;
import com.readjoy.readjoyapi.common.pojo.Book;
import org.apache.ibatis.annotations.Mapper;

/**
* @author 13296
* @description 针对表【book(书籍表)】的数据库操作Mapper
* @createDate 2024-12-07 17:55:09
* @Entity com.readjoy.readjoyapi.common.pojo.Book
*/
@Mapper
public interface BookMapper extends MPJBaseMapper<Book> {

}




