package com.readjoy.readjoyapi.common.mapper;

import com.github.yulichang.base.MPJBaseMapper;
import com.readjoy.readjoyapi.common.pojo.Category;
import com.readjoy.readjoyapi.common.vo.category.CategoryVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @author 13296
 * @description 针对表【category(分类表)】的数据库操作Mapper
 * @createDate 2024-12-07 17:55:09
 * @Entity com.readjoy.readjoyapi.common.pojo.Category
 */
@Mapper
public interface CategoryMapper extends MPJBaseMapper<Category> {

    /**
     * @param categoryId 分类ID
     * @param keyword    关键字
     * @return java.util.List<com.readjoy.readjoyapi.common.vo.category.CategoryVO>
     * @description 条件查询分类及其对应的书籍数量
     * @author 13296
     * @createDate 2024-12-07 17:55:09
     */
    List<CategoryVO> getCategoryAndBookCountByKeyword(@Param("categoryId") Integer categoryId, @Param("keyword") String keyword);

}




