package com.readjoy.readjoyapi.controller.backend.user;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.readjoy.readjoyapi.common.dto.user.SelectUserPageDTO;
import com.readjoy.readjoyapi.service.UserService;
import com.readjoy.readjoyapi.common.utils.Result;
import com.readjoy.readjoyapi.common.vo.user.UserInfoVO;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.annotation.Resource;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Tag;
import org.springframework.web.bind.annotation.*;

import static com.readjoy.readjoyapi.common.utils.UserTokenUtil.HEADER_NAME;

/**
 * 管理员模块/用户管理
 *
 * @className: AdminUserController
 * @author: Kiwi23333
 * @description: 管理员模块/用户管理
 * @date: 2024/12/7 16:41
 */

@RestController
@RequestMapping("/admin/user")
@Tag("管理员模块/用户管理")
@Slf4j
public class AdminUserController {

    @Resource
    private UserService userService;


    @Operation(description = "获取用户列表（分页）")
    @GetMapping("/page")
    Result<IPage<UserInfoVO>> getUserListByPage(
            @RequestHeader(name = HEADER_NAME) String token,
            @Valid SelectUserPageDTO dto) {
        return Result.ok(userService.getPageByDTO(dto));
    }

    @Operation(description = "批量删除用户")
    @DeleteMapping("/batch")
    Result<Integer> batchDeleteUser(
            @RequestHeader(name = HEADER_NAME) String token,
            @RequestParam("ids") Integer[] ids
    ) {
        return Result.ok(userService.batchDeleteUser(ids));
    }

    @Operation(description = "修改用户审核状态（禁用/启用）")
    @PutMapping("/{id}")
    Result<Integer> deleteUser(
            @RequestHeader(name = HEADER_NAME) String token,
            @PathVariable("id") Integer id,
            @RequestParam("status") Integer status) {
        return Result.ok(userService.updateUserStatus(id, status));
    }

}
