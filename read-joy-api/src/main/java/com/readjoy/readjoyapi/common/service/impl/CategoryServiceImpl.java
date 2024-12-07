package com.readjoy.readjoyapi.common.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.readjoy.readjoyapi.common.pojo.Category;
import com.readjoy.readjoyapi.common.service.CategoryService;
import com.readjoy.readjoyapi.common.mapper.CategoryMapper;
import org.springframework.stereotype.Service;

/**
* @author 13296
* @description 针对表【category(分类表)】的数据库操作Service实现
* @createDate 2024-12-07 17:55:09
*/
@Service
public class CategoryServiceImpl extends ServiceImpl<CategoryMapper, Category>
    implements CategoryService{

}




