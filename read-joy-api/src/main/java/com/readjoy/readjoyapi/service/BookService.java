package com.readjoy.readjoyapi.service;


import com.baomidou.mybatisplus.core.metadata.IPage;
import com.readjoy.readjoyapi.common.dto.book.InsertBookDTO;
import com.readjoy.readjoyapi.common.dto.book.SelectBookDTO;
import com.readjoy.readjoyapi.common.dto.book.UpdateBookDTO;
import com.readjoy.readjoyapi.common.vo.book.BookDetailVO;
import com.readjoy.readjoyapi.common.vo.book.BookVO;

/**
 * 图书服务层
 *
 * @author 13296
 * @description 针对表【book(书籍表)】的数据库操作Service
 * @createDate 2024-12-07 17:55:09
 */
public interface BookService {

    IPage<BookVO> getPageByDTO(SelectBookDTO dto);

    Integer addBook(InsertBookDTO dto);

    Integer updateBook(Integer id, UpdateBookDTO dto);

    BookDetailVO getBookDetail(Integer id);

    Integer batchDeleteBook(Integer[] longs);

}
