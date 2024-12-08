package com.readjoy.readjoyapi.common.service.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.github.yulichang.wrapper.MPJLambdaWrapper;
import com.readjoy.readjoyapi.common.dto.book.InsertBookDTO;
import com.readjoy.readjoyapi.common.dto.book.SelectBookDTO;
import com.readjoy.readjoyapi.common.dto.book.UpdateBookDTO;
import com.readjoy.readjoyapi.common.mapper.BookMapper;
import com.readjoy.readjoyapi.common.pojo.Book;
import com.readjoy.readjoyapi.common.repository.BookRepository;
import com.readjoy.readjoyapi.common.repository.CategoryRepository;
import com.readjoy.readjoyapi.common.service.BookService;
import com.readjoy.readjoyapi.common.utils.AssertUtil;
import com.readjoy.readjoyapi.common.utils.LocalFileUtil;
import com.readjoy.readjoyapi.common.vo.book.BookDetailVO;
import com.readjoy.readjoyapi.common.vo.book.BookVO;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author 13296
 * @description 针对表【book(书籍表)】的数据库操作Service实现
 * @createDate 2024-12-07 17:55:09
 */
@Service
public class BookServiceImpl extends ServiceImpl<BookMapper, Book> implements BookService {

    @Resource
    private BookRepository bookRepository;
    @Resource
    private CategoryRepository categoryRepository;
    @Resource
    private LocalFileUtil localFileUtil;

    @Override
    public IPage<BookVO> getPageByDTO(SelectBookDTO dto) {
        return bookRepository.selectPageByDTO(dto);
    }

    @Override
    public BookDetailVO getBookDetail(Integer id) {
        final BookDetailVO bookDetailVO = bookRepository.selectDetailAndResources(new Book().setBookId(id));
        AssertUtil.isNotEmpty(bookDetailVO, "查询图书不存在！");
        return bookDetailVO;
    }

    @Override
    public Integer addBook(InsertBookDTO dto) {
        // 1. 校验分类是否存在
        AssertUtil.isNotEmpty(categoryRepository.getById(dto.getCategoryId()), "分类不存在，请选择分类！");
        // 2. 保存图书封面
        String imgUrl = null;
        if (dto.getCoverImage() != null) {
            imgUrl = localFileUtil.saveFile(dto.getCoverImage());
        }
        // 3. 保存图书
        final Book book = InsertBookDTO.toBook(dto, imgUrl);
        final boolean saved = bookRepository.save(book);
        AssertUtil.isTrue(saved, "保存图书失败，请稍后重试！");
        return book.getBookId();
    }

    @Override
    public Integer updateBook(Integer id, UpdateBookDTO dto) {
        // 1. 校验分类是否存在
        if (dto.getCategoryId() != null) {
            AssertUtil.isNotEmpty(categoryRepository.getById(dto.getCategoryId()), "分类不存在，请选择分类！");
        }
        // 2. 保存图书封面
        String imgUrl = null;
        if (dto.getCoverImage() != null) {
            imgUrl = localFileUtil.saveFile(dto.getCoverImage());
        }
        // 3. 保存图书
        final Book book = UpdateBookDTO.toBook(dto, imgUrl);
        final boolean updated = bookRepository.update(book, new MPJLambdaWrapper<Book>().eq(Book::getBookId, id));
        AssertUtil.isTrue(updated, "保存图书失败，请稍后重试！");
        return 1;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Integer batchDeleteBook(Integer[] longs) {
        AssertUtil.isTrue(longs.length > 0, "删除图书不能为空！");
        AssertUtil.isTrue(longs.length < 500, "一次最多只能删除500本图书！");
        final Integer integer = bookRepository.batchDelete(longs);
        AssertUtil.isTrue(integer > 0 && integer == longs.length, "删除图书失败，请稍后重试！");
        return integer;
    }

}




