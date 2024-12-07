package com.readjoy.readjoyapi.common.repository;

import com.baomidou.mybatisplus.extension.repository.CrudRepository;
import com.readjoy.readjoyapi.common.mapper.CategoryMapper;
import com.readjoy.readjoyapi.common.pojo.Category;
import org.springframework.stereotype.Component;

/**
 * @className: CategoryRepository
 * @author: Kiwi23333
 * @description: TODO描述
 * @date: 2024/12/7 18:22
 */
@Component
public class CategoryRepository extends CrudRepository<CategoryMapper, Category> {

}
