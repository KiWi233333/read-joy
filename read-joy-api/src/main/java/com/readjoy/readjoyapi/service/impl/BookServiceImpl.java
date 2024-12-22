package com.readjoy.readjoyapi.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.github.yulichang.wrapper.MPJLambdaWrapper;
import com.readjoy.readjoyapi.common.dto.book.InsertBookDTO;
import com.readjoy.readjoyapi.common.dto.book.SelectBookDTO;
import com.readjoy.readjoyapi.common.dto.book.UpdateBookDTO;
import com.readjoy.readjoyapi.common.enums.BoolEnum;
import com.readjoy.readjoyapi.common.mapper.BookMapper;
import com.readjoy.readjoyapi.common.pojo.Book;
import com.readjoy.readjoyapi.common.pojo.Resource;
import com.readjoy.readjoyapi.common.utils.AssertUtil;
import com.readjoy.readjoyapi.common.utils.LocalFileUtil;
import com.readjoy.readjoyapi.common.utils.RequestHolderUtil;
import com.readjoy.readjoyapi.common.vo.book.BookDetailVO;
import com.readjoy.readjoyapi.common.vo.book.BookVO;
import com.readjoy.readjoyapi.common.vo.resource.AdminResourceVO;
import com.readjoy.readjoyapi.repository.BookRepository;
import com.readjoy.readjoyapi.repository.CategoryRepository;
import com.readjoy.readjoyapi.repository.ResourceRepository;
import com.readjoy.readjoyapi.service.BookService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * @author 13296
 * @description 针对表【book(书籍表)】的数据库操作Service实现
 * @createDate 2024-12-07 17:55:09
 */
@Service
public class BookServiceImpl extends ServiceImpl<BookMapper, Book> implements BookService {

    @jakarta.annotation.Resource
    private BookRepository bookRepository;
    @jakarta.annotation.Resource
    private CategoryRepository categoryRepository;
    @jakarta.annotation.Resource
    private ResourceRepository resourceRepository;
    @jakarta.annotation.Resource
    private LocalFileUtil localFileUtil;

    @Override
    public IPage<BookVO> getAdminPageByDTO(SelectBookDTO<BookVO> dto) {
        return bookRepository.selectPageByDTO(dto);
    }

    @Override
    public IPage<BookDetailVO> getBookDetailListByPage(SelectBookDTO<BookDetailVO> dto) {
        final IPage<BookDetailVO> pageInfo = bookRepository.getAdminPageByDTO(dto);
        // 单独查询资源列表
        final List<Integer> collect = pageInfo.getRecords().stream().map(BookDetailVO::getBookId).toList();
        if (!collect.isEmpty()) {
            final Stream<AdminResourceVO> resourceList = resourceRepository.list(new LambdaQueryWrapper<Resource>()
                    .in(Resource::getBookId, collect)).stream().map(AdminResourceVO::toVO);
            // 将资源列表转换为以BookId为键的Map，以提高查找效率
            Map<Integer, List<AdminResourceVO>> resourceMap = resourceList.collect(Collectors.groupingBy(AdminResourceVO::getBookId));
            // 遍历pageInfo中的记录，并将对应的资源列表设置到BookDetailVO中
            pageInfo.getRecords().forEach(bookDetailVO -> {
                List<AdminResourceVO> resources = resourceMap.getOrDefault(bookDetailVO.getBookId(), Collections.emptyList());
                bookDetailVO.setResourceList(resources);
            });
        }
        return pageInfo;
    }

    @Override
    public IPage<BookVO> getPageByDTO(SelectBookDTO<BookVO> dto) {
        return bookRepository.selectPageByDTO(dto);
    }

    @Override
    public BookDetailVO getBookDetail(Integer id) {
        final BookDetailVO bookDetailVO = bookRepository.selectDetailAndResources(new Book().setBookId(id), BoolEnum.FALSE);
        AssertUtil.isNotEmpty(bookDetailVO, "查询图书不存在！");
        return bookDetailVO;
    }

    @Override
    public BookDetailVO getAdminBookDetail(Integer id) {
        final BookDetailVO bookDetailVO = bookRepository.selectDetailAndResources(new Book().setBookId(id), null);
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
        // 1. 查询图书封面
        final List<Book> list = bookRepository.list(new LambdaQueryWrapper<Book>().in(Book::getBookId, Arrays.asList(longs)).select(Book::getCoverImageUrl, Book::getBookId));

        final Integer integer = bookRepository.batchDelete(longs);
        AssertUtil.isTrue(integer > 0 && integer == longs.length, "删除图书失败，请稍后重试！");
        // 2. 删除图书封面
        for (Book book : list)
            if (StringUtils.isNotBlank(book.getCoverImageUrl())) localFileUtil.deleteFile(book.getCoverImageUrl());
        return integer;
    }

}
