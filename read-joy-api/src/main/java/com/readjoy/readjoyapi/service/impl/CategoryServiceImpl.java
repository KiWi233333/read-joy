package com.readjoy.readjoyapi.service.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.readjoy.readjoyapi.common.dto.category.AdminCategoryDTO;
import com.readjoy.readjoyapi.common.dto.category.SelectAdminCategoryDTO;
import com.readjoy.readjoyapi.common.dto.category.SelectCategoryDTO;
import com.readjoy.readjoyapi.common.mapper.CategoryMapper;
import com.readjoy.readjoyapi.common.pojo.Category;
import com.readjoy.readjoyapi.common.utils.AssertUtil;
import com.readjoy.readjoyapi.common.vo.category.CategoryVO;
import com.readjoy.readjoyapi.repository.CategoryRepository;
import com.readjoy.readjoyapi.service.CategoryService;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.util.List;

/**
 * @author 13296
 * @description 针对表【category(分类表)】的数据库操作Service实现
 * @createDate 2024-12-07 17:55:09
 */
@Service
public class CategoryServiceImpl extends ServiceImpl<CategoryMapper, Category> implements CategoryService {

    @Resource
    private CategoryRepository categoryRepository;

    /**
     * 获取所有分类
     *
     * @param dto 条件查询DTO
     * @return 分类列表
     */
    @Override
    public List<CategoryVO> getListByDTO(SelectCategoryDTO dto) {
        return categoryRepository.getListByDTO(dto);
    }

    /**
     * 获取分页分类列表
     *
     * @param dto 条件查询DTO
     * @return 分页分类列表
     */
    @Override
    public IPage<CategoryVO> getPageByDTO(SelectAdminCategoryDTO dto) {
        return categoryRepository.getPageByDTO(dto);
    }


    /**
     * 新增分类
     *
     * @param dto 分类VO
     * @return 新增结果
     */
    @Override
    public Integer addCategory(AdminCategoryDTO dto) {
        final boolean save = categoryRepository.save(AdminCategoryDTO.toEntity(dto));
        AssertUtil.isTrue(save, "新增分类失败");
        return 1;
    }

    /**
     * 更新分类
     *
     * @param id         分类ID
     * @param categoryVO 分类VO
     * @return 更新结果
     */
    @Override
    public Integer updateCategory(Integer id, AdminCategoryDTO categoryVO) {
        final boolean update = categoryRepository.updateById(AdminCategoryDTO.toEntity(categoryVO, id));
        AssertUtil.isTrue(update, "分类不存在，更新失败！");
        return 1;
    }

    /**
     * 批量删除分类
     *
     * @param ids 分类ID数组
     * @return 删除结果
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public Integer batchDeleteCategory(Integer[] ids) {
        AssertUtil.isTrue(ids.length > 0, "请选择需要删除的分类！");
        final int i = categoryRepository.getBaseMapper().deleteByIds(CollectionUtils.arrayToList(ids));
        AssertUtil.isTrue(i > 0, "删除分类失败，请稍后再试！");
        AssertUtil.isTrue(i == ids.length, "删除部分分类失败，请稍后再试！");
        return i;
    }
}





