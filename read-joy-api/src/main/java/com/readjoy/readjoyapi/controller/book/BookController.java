package com.readjoy.readjoyapi.controller.book;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.readjoy.readjoyapi.common.dto.book.SelectBookDTO;
import com.readjoy.readjoyapi.service.BookService;
import com.readjoy.readjoyapi.common.utils.Result;
import com.readjoy.readjoyapi.common.vo.book.BookDetailVO;
import com.readjoy.readjoyapi.common.vo.book.BookVO;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.annotation.Resource;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Tag;
import org.springframework.web.bind.annotation.*;

/**
 * 图书模块
 *
 * @className: BookController
 * @author: Kiwi23333
 * @date: 2024/12/7 16:30
 */
@RestController
@RequestMapping("/book")
@Tag("图书模块")
@Slf4j
public class BookController {
    @Resource
    private BookService bookService;

    @Operation(description = "获取图书列表（分页）")
    @GetMapping("/page")
    Result<IPage<BookVO>> getBookListByPage(
            @Valid SelectBookDTO<BookVO> dto) {
        return Result.ok(bookService.getPageByDTO(dto));
    }

    @Operation(description = "获取图书详情")
    @GetMapping("/{id}")
    Result<BookDetailVO> getBookDetail(
            @PathVariable("id") Integer id) {
        return Result.ok(bookService.getBookDetail(id));
    }
}
