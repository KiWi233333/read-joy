package com.readjoy.readjoyapi.controller.backend.book;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.readjoy.readjoyapi.common.dto.category.AdminCategoryDTO;
import com.readjoy.readjoyapi.common.dto.category.SelectAdminCategoryDTO;
import com.readjoy.readjoyapi.common.utils.Result;
import com.readjoy.readjoyapi.common.vo.category.CategoryVO;
import com.readjoy.readjoyapi.service.CategoryService;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.annotation.Resource;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Tag;
import org.springframework.web.bind.annotation.*;

import static com.readjoy.readjoyapi.common.utils.UserTokenUtil.HEADER_NAME;

/**
 * 管理员模块/类别管理
 *
 * @className: AdminBookCategoryController
 * @author: Kiwi23333
 * @date: 2024/12/7 16:30
 */
@RestController
@RequestMapping("/admin/book/category")
@Tag("图书模块/类别管理")
@Slf4j
public class AdminBookCategoryController {

    @Resource
    private CategoryService categoryService;

    @Operation(description = "获取分类列表（分页）")
    @GetMapping("/page")
    Result<IPage<CategoryVO>> getCategoryList(
            @RequestHeader(name = HEADER_NAME) String token,
            @Valid SelectAdminCategoryDTO dto) {
        return Result.ok(categoryService.getPageByDTO(dto));
    }

    @Operation(description = "添加分类")
    @PostMapping("")
    Result<Integer> addCategory(
            @RequestHeader(name = HEADER_NAME) String token,
            @Valid @RequestBody AdminCategoryDTO dto) {
        return Result.ok(categoryService.addCategory(dto));
    }
    @Operation(description = "修改分类")
    @PutMapping("/{id}")
    Result<Integer> updateCategory(
            @RequestHeader(name = HEADER_NAME) String token,
            @PathVariable("id") Integer id,
            @Valid @RequestBody AdminCategoryDTO dto) {
        return Result.ok(categoryService.updateCategory(id, dto));
    }

    @Operation(description = "删除分类")
    @DeleteMapping("/{id}")
    Result<Integer> deleteCategory(
            @RequestHeader(name = HEADER_NAME) String token,
            @PathVariable("id") Integer id) {
        return Result.ok(categoryService.batchDeleteCategory(new Integer[]{id}));
    }

    @Operation(description = "批量删除分类")
    @DeleteMapping("/batch")
    Result<Integer> batchDeleteCategory(
            @RequestHeader(name = HEADER_NAME) String token,
            @RequestParam("ids") Integer[] ids) {
        return Result.ok(categoryService.batchDeleteCategory(ids));
    }


}
