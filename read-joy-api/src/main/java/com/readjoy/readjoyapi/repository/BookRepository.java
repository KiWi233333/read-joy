package com.readjoy.readjoyapi.repository;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.github.yulichang.repository.JoinCrudRepository;
import com.github.yulichang.wrapper.MPJLambdaWrapper;
import com.readjoy.readjoyapi.common.dto.book.SelectBookDTO;
import com.readjoy.readjoyapi.common.enums.BoolEnum;
import com.readjoy.readjoyapi.common.mapper.BookMapper;
import com.readjoy.readjoyapi.common.pojo.Book;
import com.readjoy.readjoyapi.common.pojo.Category;
import com.readjoy.readjoyapi.common.pojo.Resource;
import com.readjoy.readjoyapi.common.vo.book.BookDetailVO;
import com.readjoy.readjoyapi.common.vo.book.BookVO;
import com.readjoy.readjoyapi.common.vo.other.*;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.util.List;

/**
 * @className: BookRepository
 * @author: Kiwi23333
 * @description: TODO描述
 * @date: 2024/12/7 18:22
 */
@Component
public class BookRepository extends JoinCrudRepository<BookMapper, Book> {

    // 用户筛选图书分页
    public IPage<BookVO> selectPageByDTO(SelectBookDTO<BookVO> dto) {
        final MPJLambdaWrapper<Book> qw = new MPJLambdaWrapper<Book>()
                .selectAll(Book.class)
                .selectAs(Category::getCategoryName, BookVO::getCategoryName) // 分类名称
                .and(StringUtils.isNotBlank(dto.getKeyword()), q -> q // 关键字
                        .or().like(Book::getTitle, dto.getKeyword())
                        .or().like(Book::getIntroduction, dto.getKeyword())
                        .or().like(Book::getIsbn, dto.getKeyword())
                        .or().like(Category::getCategoryName, dto.getKeyword())
                        .or().like(Book::getAuthor, dto.getKeyword())
                )
                .eq(dto.getCategoryId() != null, Book::getCategoryId, dto.getCategoryId())
                .leftJoin(Category.class, Category::getCategoryId, Book::getCategoryId);
        if (dto.getSortType() != null && dto.getSortOrder() != null) {
            qw.orderBy(dto.checkIsSortByPubDate(), dto.checkAsc(), Book::getPublishionDate);
            qw.orderBy(dto.checkIsSortByPrice(), dto.checkAsc(), Book::getPrice);
        }
        if (dto.getStartDate() != null && dto.getEndDate() != null) {
            qw.between(Book::getPublishionDate, dto.getStartDate(), dto.getEndDate());
        }
        return this.selectJoinListPage(dto.toPage(), BookVO.class, qw);
    }


    public IPage<BookDetailVO> getAdminPageByDTO(SelectBookDTO<BookDetailVO> dto) {
        final MPJLambdaWrapper<Book> qw = new MPJLambdaWrapper<Book>()
                .selectAll(Book.class)
                .selectAs(Category::getCategoryName, BookDetailVO::getCategoryName) // 分类名称
                .and(StringUtils.isNotBlank(dto.getKeyword()), q -> q // 关键字
                        .or().like(Book::getTitle, dto.getKeyword())
                        .or().like(Book::getIntroduction, dto.getKeyword())
                        .or().like(Book::getIsbn, dto.getKeyword())
                        .or().like(Category::getCategoryName, dto.getKeyword())
                        .or().like(Book::getAuthor, dto.getKeyword())
                )
                .eq(dto.getCategoryId() != null, Book::getCategoryId, dto.getCategoryId())
                .leftJoin(Category.class, Category::getCategoryId, Book::getCategoryId);
//                .leftJoin(Resource.class, Resource::getBookId, Book::getBookId);
        if (dto.getSortType() != null && dto.getSortOrder() != null) {
            qw.orderBy(dto.checkIsSortByPubDate(), dto.checkAsc(), Book::getPublishionDate);
            qw.orderBy(dto.checkIsSortByPrice(), dto.checkAsc(), Book::getPrice);
        }
        if (dto.getStartDate() != null && dto.getEndDate() != null) {
            qw.between(Book::getPublishionDate, dto.getStartDate(), dto.getEndDate());
        }
        return this.selectJoinListPage(dto.toPage(), BookDetailVO.class, qw);
    }


    /**
     * 根据id查询书籍详情 和 资源
     *
     * @param book      图书实体
     *                  图书id
     * @param isDeleted 图书是否删除
     * @return BookDetailVO
     */
    public BookDetailVO selectDetailAndResources(Book book, BoolEnum isDeleted) {
        final MPJLambdaWrapper<Book> qw = new MPJLambdaWrapper<Book>()
                .selectAll(Book.class)
                .selectAs(Category::getCategoryName, BookDetailVO::getCategoryName)
                .leftJoin(Category.class, Category::getCategoryId, Book::getCategoryId)
                .leftJoin(Resource.class, Resource::getBookId, Book::getBookId)
                // 资源
                .selectCollection(Resource.class, BookDetailVO::getResourceList)
                // 图书详情
                .eq(book.getBookId() != null, Book::getBookId, book.getBookId())
                .eq(StringUtils.isNotBlank(book.getIsbn()), Book::getIsbn, book.getIsbn());
        final BookDetailVO bookDetailVO = this.selectJoinOne(BookDetailVO.class, qw);
        if (isDeleted != null) { // 图书资源是否删除
            bookDetailVO.getResourceList().removeIf(resource -> !resource.getIsDeleted().equals(isDeleted.getValue()));
        }
        return bookDetailVO;
    }

    public Integer batchDelete(Integer[] longs) {
        return this.getBaseMapper().deleteByIds(CollectionUtils.arrayToList(longs));
    }


    // 图书发布统计 yyyy-MM-dd
    public List<BookPublishTotalByDay> bookPublishTotalByDay() {
        return this.getBaseMapper().bookPublishTotalByDay();
    }

    // 图书发布统计 yyyy-MM-dd
    public List<BookPublishTotalByMonth> bookPublishTotalByMonth() {
        return this.getBaseMapper().bookPublishTotalByMonth();
    }

    public List<BookPublishTotalByYear> bookPublishTotalByYear() {
        return this.getBaseMapper().bookPublishTotalByYear();
    }

}
