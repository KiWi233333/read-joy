package com.readjoy.readjoyapi.repository;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.github.yulichang.repository.JoinCrudRepository;
import com.github.yulichang.wrapper.MPJLambdaWrapper;
import com.readjoy.readjoyapi.common.dto.category.SelectAdminCategoryDTO;
import com.readjoy.readjoyapi.common.dto.category.SelectCategoryDTO;
import com.readjoy.readjoyapi.common.mapper.CategoryMapper;
import com.readjoy.readjoyapi.common.pojo.Book;
import com.readjoy.readjoyapi.common.pojo.Category;
import com.readjoy.readjoyapi.common.vo.category.CategoryVO;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @className: CategoryRepository
 * @author: Kiwi23333
 * @description: TODO描述
 * @date: 2024/12/7 18:22
 */
@Component
public class CategoryRepository extends JoinCrudRepository<CategoryMapper, Category> {

    public List<CategoryVO> getListByDTO(SelectCategoryDTO dto) {
        return this.getBaseMapper().getCategoryAndBookCountByKeyword(null, dto.getKeyword());
    }

    public IPage<CategoryVO> getPageByDTO(SelectAdminCategoryDTO dto) {
//        return this.page(new Page<>(dto.getPage(), dto.getSize()), new LambdaQueryWrapper<Category>()
//                .like(StringUtils.isNotBlank(dto.getKeyword()), Category::getCategoryName, dto.getKeyword()))
//                .convert(CategoryVO::toVO);
        return this.selectJoinListPage(dto.toPage(), CategoryVO.class, new MPJLambdaWrapper<Category>()
                .select(Category::getCategoryId, Category::getCategoryName)
                .like(StringUtils.isNotBlank(dto.getKeyword()), Category::getCategoryName, dto.getKeyword())
                .leftJoin(Book.class, Book::getCategoryId, Category::getCategoryId)
                .groupBy(Category::getCategoryId)
                .selectCount(Book::getBookId, CategoryVO::getBookCount));
    }
}
