package com.readjoy.readjoyapi.controller.backend.book;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.readjoy.readjoyapi.common.dto.book.InsertBookDTO;
import com.readjoy.readjoyapi.common.dto.book.SelectBookDTO;
import com.readjoy.readjoyapi.common.dto.book.UpdateBookDTO;
import com.readjoy.readjoyapi.common.utils.Result;
import com.readjoy.readjoyapi.common.vo.book.BookDetailVO;
import com.readjoy.readjoyapi.common.vo.book.BookVO;
import com.readjoy.readjoyapi.service.BookService;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.annotation.Resource;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Tag;
import org.springframework.web.bind.annotation.*;

import static com.readjoy.readjoyapi.common.utils.UserTokenUtil.HEADER_NAME;

/**
 * 管理员模块/图书管理
 *
 * @className: BookController
 * @author: Kiwi23333
 * @description: TODO描述
 * @date: 2024/12/7 16:30
 */
@RestController
@RequestMapping("/admin/book")
@Tag("管理员模块/图书管理")
@Slf4j
public class AdminBookController {

    @Resource
    private BookService bookService;

    @Operation(description = "获取图书列表（分页）")
    @GetMapping("/page")
    Result<IPage<BookVO>> getBookListByPage(
            @RequestHeader(name = HEADER_NAME) String token,
            @Valid SelectBookDTO<BookVO> dto) {
        return Result.ok(bookService.getAdminPageByDTO(dto));
    }

    @Operation(description = "获取图书详情列表（分页）（包含对应资源列表）")
    @GetMapping("/page/detail")
    Result<IPage<BookDetailVO>> getBookDetailListByPage(
            @RequestHeader(name = HEADER_NAME) String token,
            @Valid SelectBookDTO<BookDetailVO> dto) {
        return Result.ok(bookService.getBookDetailListByPage(dto));
    }

    @Operation(description = "获取图书详情")
    @GetMapping("/{id}")
    Result<BookDetailVO> getBookDetail(
            @RequestHeader(name = HEADER_NAME) String token,
            @PathVariable("id") Integer id) {
        return Result.ok(bookService.getBookDetail(id));
    }

    @Operation(description = "添加图书")
    @PostMapping
    Result<Integer> addBook(
            @RequestHeader(name = HEADER_NAME) String token,
            @Valid InsertBookDTO dto) {
        return Result.ok(bookService.addBook(dto));
    }

    @Operation(description = "修改图书")
    @PutMapping("/{id}")
    Result<Integer> updateBook(
            @RequestHeader(name = HEADER_NAME) String token,
            @PathVariable("id") Integer id,
            @Valid UpdateBookDTO dto) {
        return Result.ok(bookService.updateBook(id, dto));
    }

    @Operation(description = "删除图书")
    @DeleteMapping("/{id}")
    Result<Integer> deleteBook(
            @RequestHeader(name = HEADER_NAME) String token,
            @PathVariable("id") Integer id) {
        return Result.ok(bookService.batchDeleteBook(new Integer[]{id}));
    }

    @Operation(description = "批量删除图书")
    @DeleteMapping("/batch")
    Result<Integer> batchDeleteBook(
            @RequestHeader(name = HEADER_NAME) String token,
            @RequestParam("ids") Integer[] ids
    ) {
        return Result.ok(bookService.batchDeleteBook(ids));
    }
}
