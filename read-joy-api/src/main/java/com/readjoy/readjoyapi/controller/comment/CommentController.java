package com.readjoy.readjoyapi.controller.comment;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.readjoy.readjoyapi.common.dto.comment.AdminSelectCommentDTO;
import com.readjoy.readjoyapi.common.dto.comment.InsertCommentDTO;
import com.readjoy.readjoyapi.common.dto.comment.SelectCommentDTO;
import com.readjoy.readjoyapi.common.utils.RequestHolderUtil;
import com.readjoy.readjoyapi.common.utils.Result;
import com.readjoy.readjoyapi.common.vo.comment.CommentVO;
import com.readjoy.readjoyapi.service.CommentService;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.annotation.Resource;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Tag;
import org.springframework.web.bind.annotation.*;

import static com.readjoy.readjoyapi.common.utils.UserTokenUtil.HEADER_NAME;

/**
 * 评论模块
 *
 * @className: BookController
 * @author: Kiwi23333
 * @date: 2024/12/7 16:30
 */
@RestController
@RequestMapping("/comment")
@Tag("评论模块")
@Slf4j
public class CommentController {
    @Resource
    private CommentService commentService;

    @Operation(description = "获取评论列表（分页）")
    @GetMapping("/{bookId}")
    Result<IPage<CommentVO>> getBookListByPage(
            @RequestHeader(name = HEADER_NAME) String token,
            @PathVariable("bookId") Integer bookId,
            @Valid SelectCommentDTO dto) {
        Integer userId = RequestHolderUtil.get().getId();
        AdminSelectCommentDTO aDTO = AdminSelectCommentDTO.toViewDTO(dto, bookId, userId);
        return Result.ok(commentService.getPageByDTO(aDTO));
    }

    @Operation(description = "获取评论列表（分页）（本人评论管理）")
    @GetMapping("/self")
    Result<IPage<CommentVO>> getSelfCommentListByPage(
            @RequestHeader(name = HEADER_NAME) String token,
            @Valid SelectCommentDTO dto) {
        AdminSelectCommentDTO aDTO = AdminSelectCommentDTO.toUserDTO(dto, RequestHolderUtil.get().getId());
        return Result.ok(commentService.getPageByDTO(aDTO));
    }

    @Operation(description = "添加评论")
    @PostMapping("")
    Result<CommentVO> addComment(
            @RequestHeader(name = HEADER_NAME) String token,
            @Valid @RequestBody InsertCommentDTO dto) {
        return Result.ok(commentService.addComment(dto));
    }

    @Operation(description = "删除评论")
    @DeleteMapping("/{id}")
    Result<Integer> deleteComment(
            @RequestHeader(name = HEADER_NAME) String token,
            @PathVariable("id") Integer id) {
        Integer userId = RequestHolderUtil.get().getId();
        return Result.ok(commentService.deleteComment(userId, id));
    }
}
