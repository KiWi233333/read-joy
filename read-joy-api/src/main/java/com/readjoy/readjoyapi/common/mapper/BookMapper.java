package com.readjoy.readjoyapi.common.mapper;

import com.github.yulichang.base.MPJBaseMapper;
import com.readjoy.readjoyapi.common.pojo.Book;
import com.readjoy.readjoyapi.common.vo.other.AdminHomeStatisticsVO;
import com.readjoy.readjoyapi.common.vo.other.BookPublishTotalByDay;
import com.readjoy.readjoyapi.common.vo.other.BookPublishTotalByMonth;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
* @author 13296
* @description 针对表【book(书籍表)】的数据库操作Mapper
* @createDate 2024-12-07 17:55:09
*/
@Mapper
public interface BookMapper extends MPJBaseMapper<Book> {

    List<BookPublishTotalByDay> bookPublishTotalByDay();

    List<BookPublishTotalByMonth> bookPublishTotalByMonth();
}




