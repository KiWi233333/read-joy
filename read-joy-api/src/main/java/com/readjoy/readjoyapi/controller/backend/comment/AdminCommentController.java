package com.readjoy.readjoyapi.controller.backend.comment;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.readjoy.readjoyapi.common.dto.comment.AdminSelectCommentDTO;
import com.readjoy.readjoyapi.common.enums.CommentStatusEnum;
import com.readjoy.readjoyapi.common.utils.AssertUtil;
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
 * 管理员模块/评论模块
 *
 * @className: BookController
 * @author: Kiwi23333
 * @date: 2024/12/7 16:30
 */
@RestController
@RequestMapping("/admin/comment")
@Tag("管理员模块/评论模块")
@Slf4j
public class AdminCommentController {
    @Resource
    private CommentService commentService;

    @Operation(description = "获取评论列表（分页）")
    @GetMapping("/page")
    Result<IPage<CommentVO>> getBookListByPage(
            @RequestHeader(name = HEADER_NAME) String token,
            @Valid AdminSelectCommentDTO dto) {
        return Result.ok(commentService.getPageByDTO(dto));
    }

    @Operation(description = "删除评论")
    @DeleteMapping("/{id}")
    Result<Integer> deleteComment(
            @RequestHeader(name = HEADER_NAME) String token,
            @PathVariable("id") Integer id) {
        Integer userId = RequestHolderUtil.get().getId();
        return Result.ok(commentService.deleteComment(userId, id));
    }

    @Operation(description = "修改评论审核状态")
    @PutMapping("/{id}/{status}")
    Result<Integer> updateCommentStatus(
            @RequestHeader(name = HEADER_NAME) String token,
            @PathVariable("id") Integer id,
            @PathVariable("status") Integer status) {
        AssertUtil.isTrue(CommentStatusEnum.fromCode(status) != null, "修改状态码错误，请检查！");
        return Result.ok(commentService.updateCommentStatus(id, status));
    }

}
