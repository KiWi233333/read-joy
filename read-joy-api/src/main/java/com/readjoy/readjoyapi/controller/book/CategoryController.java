package com.readjoy.readjoyapi.controller.book;

import com.readjoy.readjoyapi.common.dto.category.SelectCategoryDTO;
import com.readjoy.readjoyapi.service.CategoryService;
import com.readjoy.readjoyapi.common.utils.Result;
import com.readjoy.readjoyapi.common.vo.category.CategoryVO;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.annotation.Resource;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Tag;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * 类别模块
 *
 * @className: BookController
 * @author: Kiwi23333
 * @description: TODO描述
 * @date: 2024/12/7 16:30
 */
@RestController
@RequestMapping("/book/category")
@Tag("类别模块")
@Slf4j
public class CategoryController {

    @Resource
    private CategoryService categoryService;

    @Operation(description = "获取所有类别列表")
    @GetMapping("/list")
    Result<List<CategoryVO>> getCategoryList(
            @Valid SelectCategoryDTO dto) {
        return Result.ok(categoryService.getListByDTO(dto));
    }


}
