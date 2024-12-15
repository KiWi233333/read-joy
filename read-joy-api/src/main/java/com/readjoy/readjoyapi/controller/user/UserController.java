package com.readjoy.readjoyapi.controller.user;

import com.readjoy.readjoyapi.common.dto.user.UserLoginDTO;
import com.readjoy.readjoyapi.common.dto.user.UserRegisterDTO;
import com.readjoy.readjoyapi.common.dto.user.UserUpdateInfoDTO;
import com.readjoy.readjoyapi.common.dto.user.UserUpdatePwdDTO;
import com.readjoy.readjoyapi.common.utils.Result;
import com.readjoy.readjoyapi.common.vo.user.UserInfoVO;
import com.readjoy.readjoyapi.common.vo.user.UserLoginVO;
import com.readjoy.readjoyapi.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import jakarta.annotation.Resource;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Tag;
import org.springframework.web.bind.annotation.*;

import static com.readjoy.readjoyapi.common.utils.UserTokenUtil.HEADER_NAME;

/**
 * 用户模块
 *
 * @className: UserController
 * @author: Kiwi23333
 * @date: 2024/12/7 16:30
 */
@RestController
@RequestMapping("/user")
@Tag("用户模块")
@Slf4j
public class UserController {

    @Resource
    private UserService userService;

    @PostMapping("/login")
    @Operation(summary = "用户登录")
    Result<UserLoginVO> login(@Valid @RequestBody UserLoginDTO loginDTO) {
        return Result.ok(userService.login(loginDTO));
    }

    @GetMapping("")
    @Operation(summary = "获取用户信息")
    @Parameter(name = HEADER_NAME, in = ParameterIn.HEADER, description = "token", required = true)
    public Result<UserInfoVO> getUserInfo(@RequestHeader(name = HEADER_NAME) String token) {
        return Result.ok(userService.getUserInfo());
    }

    @PostMapping("/register")
    @Operation(summary = "用户注册（返回登录信息）")
    Result<UserLoginVO> register(@Valid @RequestBody UserRegisterDTO loginDTO) {
        return Result.ok(userService.register(loginDTO));
    }

    @PutMapping("")
    @Operation(summary = "修改用户基本信息")
    @Parameter(name = HEADER_NAME, in = ParameterIn.HEADER, description = "token", required = true)
    public Result<UserInfoVO> updateUser(@Valid UserUpdateInfoDTO dto, @RequestHeader(name = HEADER_NAME) String token) {
        return Result.ok(userService.updateUserBaseInfo(dto));
    }

    @PutMapping("/pwd")
    @Operation(summary = "修改密码")
    @Parameter(name = HEADER_NAME, in = ParameterIn.HEADER, description = "token", required = true)
    public Result<Integer> updatePwd(@Valid @RequestBody UserUpdatePwdDTO dto, @RequestHeader(name = HEADER_NAME) String token) {
        return Result.ok(userService.updatePwd(dto));
    }
}
