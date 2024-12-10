package com.readjoy.readjoyapi.service;


import com.baomidou.mybatisplus.core.metadata.IPage;
import com.readjoy.readjoyapi.common.dto.category.AdminCategoryDTO;
import com.readjoy.readjoyapi.common.dto.category.SelectAdminCategoryDTO;
import com.readjoy.readjoyapi.common.dto.category.SelectCategoryDTO;
import com.readjoy.readjoyapi.common.vo.category.CategoryVO;

import java.util.List;

/**
 * 图书分类服务层
 *
 * @author 13296
 * @description 针对表【category(分类表)】的数据库操作Service
 * @createDate 2024-12-07 17:55:09
 */
public interface CategoryService {
    /**
     * 新增分类
     *
     * @param categoryVO 分类VO
     * @return 新增结果
     */
    Integer addCategory(AdminCategoryDTO categoryVO);


    /**
     * 获取所有分类
     *
     * @param dto 条件查询DTO
     * @return 分类列表
     */
    List<CategoryVO> getListByDTO(SelectCategoryDTO dto);

    /**
     * 获取分页分类列表
     *
     * @param dto 条件查询DTO
     * @return 分页分类列表
     */
    IPage<CategoryVO> getPageByDTO(SelectAdminCategoryDTO dto);

    /**
     * 批量删除分类
     *
     * @param ids 分类ID数组
     * @return 删除结果
     */
    Integer batchDeleteCategory(Integer[] ids);


    /**
     * 修改分类
     *
     * @param id 分类ID
     * @param dto 分类DTO
     * @return 修改结果
     */
    Integer updateCategory(Integer id, AdminCategoryDTO dto);
}
